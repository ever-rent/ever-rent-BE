package com.finalproject.everrent_be.domain.product.repository;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCateId(String cateId);
    List<Product> findAllByOrderByCreatedAtDesc();
    List<Product> findAllByLocationOrLocationOrderByCreatedAt(String mainAddress,String subAddress);
    List<Product> findFirst8ByOrderByWishNumDesc();
    List<Product> findFirst8ByLocationOrLocationOrderByWishNumDesc(String mainAddress,String subAddress);

    List<Product> findAllByMemberId(Long id);


    //repository명.findAll(Sort.by(Sort.Direction.DESC, "기준컬럼명"));
}