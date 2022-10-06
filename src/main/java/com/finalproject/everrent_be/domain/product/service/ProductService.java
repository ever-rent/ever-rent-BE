package com.finalproject.everrent_be.domain.product.service;

import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.global.common.ResponseDto;
import com.finalproject.everrent_be.global.jwt.TokenProvider;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.global.common.Status;
import com.finalproject.everrent_be.domain.wishlist.model.WishList;
import com.finalproject.everrent_be.domain.product.dto.ProductRequestDto;
import com.finalproject.everrent_be.domain.product.dto.ProductResponseDto;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.wishlist.repository.WishListRepository;
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
    public final FileUploadService fileUploadService;
    public final TokenProvider tokenProvider;

    public final WishListRepository wishListRepository;

    public ResponseDto<?> getAllProduct(String page) {

        List<ProductResponseDto> responseDtos =new ArrayList<>();
        List<Product> productList;

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userId.equals("anonymousUser")){
            productList=productRepository.findAll();
        }
        else {
            Optional<Member> optionalMember = memberRepository.findById(Long.valueOf(userId));  //Long.valueOf(userId)
            Member member=optionalMember.get();
            productList=productRepository.findAllByLocationOrLocation(member.getMainAddress(), member.getSubAddress());
        }

        int startIdx=(Integer.valueOf(page)-1)*12;
        int lastIdx=Integer.valueOf(page)*12;
        try{
            Product product=productList.get(lastIdx);
        }catch (Exception e){
            lastIdx=productList.size();
        }
        for(int i=startIdx;i<lastIdx;i++){
            responseDtos.add(new ProductResponseDto(productList.get(i)));
        }

        return ResponseDto.is_Success(responseDtos);
    }


    public ResponseDto<?> getProduct(String productId){

        Product product = productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);

    }

    public ResponseDto<?> getFromCategory(String cateId,String page){
        List<Product> productList=productRepository.findAllByCateId(cateId);
        List<ProductResponseDto> responseDtos =new ArrayList<>();

        int startIdx=(Integer.valueOf(page)-1)*12;
        int lastIdx=Integer.valueOf(page)*12;
        for(int i=startIdx;i<lastIdx;i++){
            responseDtos.add(new ProductResponseDto(productList.get(i)));
        }

        return ResponseDto.is_Success(responseDtos);
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
    @Transactional
    public ResponseDto<?> putWishList(String productId)
    {
        Member member=memberService.getMemberfromContext();
        WishList wishList=wishListRepository.findByMemberIdAndProductId(member.getId(), Long.valueOf(productId));
        Optional<Product> optionalProduct=productRepository.findById(Long.valueOf(productId));
        Product product=optionalProduct.get();
        if(product.getMember().equals(member))
        {
            return ResponseDto.is_Fail(INVALID_WISH);
        }
        if(wishList!=null)
        {
            wishListRepository.delete(wishList);
            return ResponseDto.is_Success("찜 등록이 취소되었습니다.");
        }
        else
        {
            wishListRepository.save(WishList.builder()
                    .member(member)
                    .product(product)
                    .build());
            return ResponseDto.is_Success("찜 등록이 완료되었습니다.");
        }


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
