package com.finalproject.everrent_be.service;

import com.finalproject.everrent_be.dto.*;
import com.finalproject.everrent_be.exception.ErrorCode;
import com.finalproject.everrent_be.jwt.TokenProvider;
import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.finalproject.everrent_be.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    public final ProductRepository productRepository;
    public final MemberService memberService;
    public final FileUploadService fileUploadService;
    public final TokenProvider tokenProvider;


    public ResponseDto<?> getAllProduct() {
        List<Product> productList=productRepository.findAll();
        List<ProductResponseDto> responseDtos =new ArrayList<>();
        for(Product product:productList){
            responseDtos.add(new ProductResponseDto(product));
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

    public ResponseDto<?> getFromCategory(String cateId){
        List<Product> productList=productRepository.findAllByCateId(cateId);
        List<ProductResponseDto> responseDtos =new ArrayList<>();


        for(Product product:productList){
            responseDtos.add(new ProductResponseDto(product));
        }
        return ResponseDto.is_Success(responseDtos);


    }



    @Transactional
    public ResponseDto<?> createProduct(MultipartFile multipartFile, ProductRequestDto requestDto, HttpServletRequest request){


        Member member= memberService.getMemberfromContext();

        if(member==null)
        {
            return ResponseDto.is_Fail(NULL_TOKEN);
        }

        Product product= Product.builder()
                .productName(requestDto.getProductName())
                .price(requestDto.getPrice())
                .content(requestDto.getContent())
                .imgUrl(fileUploadService.uploadImage(multipartFile))
                .member(member) // member-product OnetoMany
                .cateId(requestDto.getCateId())
                .rentStart(requestDto.getRentStart())
                .rentEnd(requestDto.getRentEnd())
                .confirm("1")
                .build();

        productRepository.save(product);
        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);
    }

    @Transactional
    public ResponseDto<?> updateProduct(String productId, MultipartFile multipartFile, ProductRequestDto requestDto, HttpServletRequest request){
        Product product = productRepository.findById(Long.valueOf(productId)).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Member member = product.getMember();
        if (!product.getConfirm().equals("1"))
        {
            return ResponseDto.is_Fail(INVALID_CONFIRM);
        }
        if(!verifiedMember(request,member)){
            return ResponseDto.is_Fail(MEMBER_NOT_ALLOWED);
        }

        String url=fileUploadService.uploadImage(multipartFile);
        product.update(requestDto,member,url);
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



}
