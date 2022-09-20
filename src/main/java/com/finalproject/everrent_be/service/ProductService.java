package com.finalproject.everrent_be.service;

import com.finalproject.everrent_be.dto.ProductRequestDto;
import com.finalproject.everrent_be.dto.ProductResponseDto;
import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.jwt.TokenProvider;
import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import static com.finalproject.everrent_be.exception.ErrorCode.MEMBER_NOT_ALLOWED;
import static com.finalproject.everrent_be.exception.ErrorCode.NULL_TOKEN;

@Service
@RequiredArgsConstructor
public class ProductService {

    public final ProductRepository productRepository;
    public final MemberService memberService;
    public final FileUploadService fileUploadService;

    public final TokenProvider tokenProvider;

    public ResponseDto<?> getProduct(Long productId, HttpServletRequest request){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);

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
                .cateName(requestDto.getCateName())
                .rentStart(requestDto.getRentStart())
                .rentEnd(requestDto.getRentEnd())
                .build();

        productRepository.save(product);
        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);
    }

    @Transactional
    public ResponseDto<?> updateProduct( Long productId, MultipartFile multipartFile, ProductRequestDto requestDto, HttpServletRequest request){

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Member member = product.getMember();
        if(!verifiedMember(request,member)){
            return ResponseDto.is_Fail(MEMBER_NOT_ALLOWED);
        }

        String url=fileUploadService.uploadImage(multipartFile);

        product.update(requestDto,member,url);
        ProductResponseDto productResponseDto=new ProductResponseDto(product);
        return ResponseDto.is_Success(productResponseDto);
    }

    @Transactional
    public ResponseDto<?> deleteProduct(Long productId,HttpServletRequest request){
        Product product = productRepository.findById(productId).orElseThrow(
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
