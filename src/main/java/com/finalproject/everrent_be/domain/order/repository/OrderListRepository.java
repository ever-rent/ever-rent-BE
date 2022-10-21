package com.finalproject.everrent_be.domain.order.repository;


import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.order.model.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderListRepository extends JpaRepository<OrderList, Long> {

    List<OrderList> findAllByMember(Member member);
    List<OrderList> findAllByMemberId(Long id);
    OrderList findById(String id);
}