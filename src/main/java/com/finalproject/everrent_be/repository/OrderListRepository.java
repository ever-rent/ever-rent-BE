package com.finalproject.everrent_be.repository;


import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.OrderList;
import com.finalproject.everrent_be.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderListRepository extends JpaRepository<OrderList, Long> {

    List<OrderList> findAllByMember(Member member);
}