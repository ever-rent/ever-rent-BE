package com.finalproject.everrent_be.domain.wishlist.repository;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.wishlist.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    boolean existsByMemberIdAndProductId(Long memberId,Long productId);
    WishList findByMemberIdAndProductId(Long memberId,Long productId);
    void deleteAllByProductId(Long product_id);
    List<WishList> findAllByMember(Member member);
}
