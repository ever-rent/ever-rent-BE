package com.finalproject.everrent_be.repository;

import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    boolean existsByMemberIdAndProductId(Long memberId,Long productId);
    WishList findByMemberIdAndProductId(Long memberId,Long productId);
    List<WishList> findAllByMember(Member member);
}
