package com.finalproject.everrent_be.domain.product.service;

import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.product.dto.ProductMainResponseDto;
import com.finalproject.everrent_be.domain.wishlist.repository.WishListRepository;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.finalproject.everrent_be.global.jwt.TokenProvider;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.Status;
import com.finalproject.everrent_be.domain.product.dto.ProductRequestDto;
import com.finalproject.everrent_be.domain.product.dto.ProductResponseDto;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.imageupload.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.finalproject.everrent_be.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    public final ProductRepository productRepository;
    public final MemberService memberService;
    public final MemberRepository memberRepository;
    public final WishListRepository wishListRepository;
    public final FileUploadService fileUploadService;
    public final TokenProvider tokenProvider;

    public boolean is_lastpage;

    public ResponseDto<?> getAllProduct(String page) {
        List<ProductMainResponseDto> responseDtos =new ArrayList<>();
        List<ProductMainResponseDto> bestresponseDtos=new ArrayList<>();
        List<Product> productList;
        List<Product> bestList;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        //본인지역 기준 조회
        if (userId.equals("anonymousUser")){
            productList=productRepository.findAllByOrderByCreatedAtDesc();
            bestList=productRepository.findFirst8ByOrderByWishNumDesc();
        }
        else {
            Optional<Member> optionalMember = memberRepository.findById(Long.valueOf(userId));  //Long.valueOf(userId)
            Member member=optionalMember.get();
            productList=productRepository.findAllByLocationOrLocationOrderByCreatedAt(member.getMainAddress(), member.getSubAddress());
            bestList=productRepository.findFirst8ByLocationOrLocationOrderByWishNumDesc(member.getMainAddress(), member.getSubAddress());
        }
        for(Product best:bestList){
            boolean heart=false;
            if (userId.equals("anonymousUser")){
                heart=false;
            }
            else if (wishListRepository.findByMemberIdAndProductId(Long.valueOf(userId), best.getId()) != null) {
                heart = true;
            }
            bestresponseDtos.add(new ProductMainResponseDto(best,heart));
        }

        int startIdx=(Integer.valueOf(page)-1)*12;
        int lastIdx=Integer.valueOf(page)*12;
        try{
            Product product=productList.get(lastIdx);
        }catch (Exception e){
            lastIdx=productList.size();
            is_lastpage=true;
        }
        for(int i=startIdx;i<lastIdx;i++) {
            boolean heart=false;
            Product product = productList.get(i);

            if (userId.equals("anonymousUser")){
                heart=false;
            }
            else if (wishListRepository.findByMemberIdAndProductId(Long.valueOf(userId), product.getId()) != null) {
                heart = true;
            }
            responseDtos.add(new ProductMainResponseDto(product, heart));
        }

        return ResponseDto.is_Success(bestresponseDtos,responseDtos,is_lastpage);
    }


    public ResponseDto<?> getOneProduct(String productId){
        Product product = productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        ProductResponseDto productResponseDto=new ProductResponseDto(product);

        //현재 product에서 꺼낸 멤버id에 context홀더에서 꺼낸 멤버id가 있음
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userId.equals("anonymousUser")){
            return ResponseDto.is_Success(productResponseDto);
        }
        if(wishListRepository.findByMemberIdAndProductId(Long.valueOf(userId), product.getId())!=null){
            productResponseDto.UpdateLike(true);
        }
        return ResponseDto.is_Success(productResponseDto);
    }



    public ResponseDto<?> getFromCategory(String cateId,String page){
        List<Product> productList=productRepository.findAllByCateId(cateId);
        List<ProductMainResponseDto> responseDtos =new ArrayList<>();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();


        int startIdx=(Integer.valueOf(page)-1)*12;
        int lastIdx=Integer.valueOf(page)*12;
        try{
            Product product=productList.get(lastIdx);
        }catch (Exception e){
            lastIdx=productList.size();
            is_lastpage=true;
        }
        for(int i=startIdx;i<lastIdx;i++) {
            boolean heart=false;
            Product product = productList.get(i);

            if (userId.equals("anonymousUser")){
                heart=false;
            }
            else if (wishListRepository.findByMemberIdAndProductId(Long.valueOf(userId), product.getId()) != null) {
                heart = true;
            }
            responseDtos.add(new ProductMainResponseDto(product, heart));
        }

        return ResponseDto.is_Success(null,responseDtos,is_lastpage);
    }


    @Transactional
    public ResponseDto<?> createProduct(List<MultipartFile> multipartFiles, ProductRequestDto requestDto, HttpServletRequest request){


        Member member= memberService.getMemberfromContext();
        if(member==null)
        {
            return ResponseDto.is_Fail(NULL_TOKEN);
        }

        StringBuffer sb=new StringBuffer();
        for(MultipartFile multipartFile:multipartFiles){
            String onestr=fileUploadService.uploadImage(multipartFile);
            sb.append(onestr.substring(onestr.lastIndexOf("/")+1)+' ');
        }
        Product product=new Product(requestDto,member,sb.toString(),StrToLocalDate(requestDto.getRentStart()),StrToLocalDate(requestDto.getRentEnd()));
        productRepository.save(product);
        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);
    }

    @Transactional
    public ResponseDto<?> updateProduct(String productId, MultipartFile[] multipartFiles, ProductRequestDto requestDto, HttpServletRequest request){
        Product product = productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Member member = product.getMember();
        if (!product.getStatus().equals(Status.WAITING))
        {
            return ResponseDto.is_Fail(INVALID_CONFIRM);
        }
        if(!verifiedMember(request,member)){
            return ResponseDto.is_Fail(MEMBER_NOT_ALLOWED);
        }

        StringBuffer sb=new StringBuffer();
        for(MultipartFile multipartFile:multipartFiles){
            String onestr=fileUploadService.uploadImage(multipartFile);
            sb.append(onestr.substring(onestr.lastIndexOf("/")+1)+' ');
        }
        product.update(requestDto,member,sb.toString(),StrToLocalDate(requestDto.getRentStart()),StrToLocalDate(requestDto.getRentEnd()));
        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);
    }

    @Transactional
    public ResponseDto<?> deleteProduct(String productId,HttpServletRequest request){
        Product product = productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Member member = product.getMember();
        if(!verifiedMember(request,member)){
            return ResponseDto.is_Fail(MEMBER_NOT_ALLOWED);
        }

        productRepository.delete(product);

        return ResponseDto.is_Success("삭제 완료");
    }

    /**
     * 토큰받아 본인확인 메서드
     * @param request
     * @param member
     * @return
     */

    private boolean verifiedMember(HttpServletRequest request, Member member) {
        String token = request.getHeader("Authorization").substring((7));
        if (token == null || !tokenProvider.validateToken(token)){
            return false;
        }
        Authentication authentication = tokenProvider.getAuthentication(token);
        String memberId = authentication.getName(); //member Id
        if (Long.valueOf(memberId)!= member.getId()){
            return false;
        }
        return true;
    }
    public LocalDate StrToLocalDate(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(string,formatter);
        return date;
    }
    public String LocalDateToStr(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


}
