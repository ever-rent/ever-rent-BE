package com.finalproject.everrent_be.repository;


import com.finalproject.everrent_be.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByProductId(Long productId);
}