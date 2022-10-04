package com.finalproject.everrent_be.service;

import com.finalproject.everrent_be.dto.*;
import com.finalproject.everrent_be.jwt.TokenProvider;
import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.model.Status;
import com.finalproject.everrent_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.finalproject.everrent_be.jwt.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    public final ProductRepository productRepository;
    public final MemberService memberService;
    public final FileUploadService fileUploadService;
    public final TokenProvider tokenProvider;



    public ResponseDto<?> getAllProduct(String page) {

        List<Product> productList=productRepository.findAll();
        List<ProductResponseDto> responseDtos =new ArrayList<>();
        int startIdx=(Integer.valueOf(page)-1)*12;
        int lastIdx=Integer.valueOf(page)*12;
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

        String bucket="";
        for(MultipartFile multipartFile:multipartFiles){
            bucket+=fileUploadService.uploadImage(multipartFile)+' ';
        }


        Product product=new Product(requestDto,member,bucket,StrToLocalDate(requestDto.getRentStart()),StrToLocalDate(requestDto.getRentEnd()));
        productRepository.save(product);
        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);
    }

    @Transactional
    public ResponseDto<?> updateProduct(String productId, List<MultipartFile> multipartFiles, ProductRequestDto requestDto, HttpServletRequest request){
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

        String bucket="";
        for(MultipartFile multipartFile:multipartFiles){
            bucket+=fileUploadService.uploadImage(multipartFile)+' ';
        }
        product.update(requestDto,member,bucket,StrToLocalDate(requestDto.getRentStart()),StrToLocalDate(requestDto.getRentEnd()));
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
