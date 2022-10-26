package com.finalproject.everrent_be.domain.wishlist.service;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.service.MemberService;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.domain.wishlist.model.WishList;
import com.finalproject.everrent_be.domain.wishlist.repository.WishListRepository;
import com.finalproject.everrent_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.finalproject.everrent_be.global.error.ErrorCode.INVALID_WISH;
@Service
@RequiredArgsConstructor
public class WishService {

    public final MemberService memberService;

    public final WishListRepository wishListRepository;
    public final ProductRepository productRepository;

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
            product.updateWish(-1);
            wishListRepository.delete(wishList);
            return ResponseDto.is_Success(false);
        }
        else
        {
            product.updateWish(1);
            wishListRepository.save(WishList.builder()
                    .member(member)
                    .product(product)
                    .build());
            //뱃지6-찜10개 이상
            List<WishList> mylist=wishListRepository.findAllByMember(member);
            if(mylist.size()==10){
                member.setBadges(6,"1");
            }
            return ResponseDto.is_Success(true);
        }


    }
}
