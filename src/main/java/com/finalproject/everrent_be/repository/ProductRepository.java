package com.finalproject.everrent_be.repository;

import com.finalproject.everrent_be.model.Product;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCateId(String cateId);

}