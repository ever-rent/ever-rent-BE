package com.finalproject.everrent_be.global.util;


import com.amazonaws.services.ec2.model.Reservation;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.member.repository.MemberRepository;
import com.finalproject.everrent_be.domain.order.model.OrderList;
import com.finalproject.everrent_be.domain.order.repository.OrderListRepository;
import com.finalproject.everrent_be.domain.product.model.Product;
import com.finalproject.everrent_be.domain.product.repository.ProductRepository;
import com.finalproject.everrent_be.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Check {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
//    private final PostImgUrlRepository postImgUrlRepository;
//    private final BlockDateRepository blockDateRepository;
//    private final ReviewRepository reviewRepository;
//    private final ReservationRepository reservationRepository;
//    private final ReviewImgUrlRepository reviewImgUrlRepository;
    private final OrderListRepository orderListRepository;
    private final TokenProvider tokenProvider;

    public void checkProduct(Product product) {
        if (product == null) throw new IllegalArgumentException();
    }

//    public void checkReview(Review review) {
//        if (review == null) throw new NotFoundReviewException();
//    }

    public void checkOrder(OrderList orderList) { if (orderList == null) throw new IllegalArgumentException(); }

    public void checkPostAuthor(Member member, Product product) {
        if (!product.getMember().equals(member)) throw new IllegalArgumentException();
    }

//    public void checkReviewAuthor(Member member, Review review) {
//        if (!review.getMember().equals(member)) throw new NotAuthorException();
//    }
//
//    public void checkReservationPostAuthor(Member member, Reservation reservation) {
//        if (!reservation.getJully().equals(member)) throw new IllegalArgumentException("게시글 작성자만 예약 상태를 변경할 수 있습니다.");
//    }
//
//    public void checkReservationAuthor(Member member, Reservation reservation) {
//        if (!reservation.getBilly().equals(member)) throw new IllegalArgumentException("예약 당사자만 취소할 수 있습니다.");
//    }
//
//    public void validateReservation(Reservation reservation) {
//        if (reservation.getState() == 3) throw new IllegalArgumentException("이미 취소된 예약건입니다.");
//    }
//
//    public void validateDelivery(int state, Reservation reservation) {
//        if (state == 4) {
//            if (!reservation.isDelivery()) throw new IllegalArgumentException("아직 빌리가 전달받지 않은 상태입니다.");
//        }
//    }

    public Product getCurrentProduct(String id) {
        Optional<Product> optionalPost = productRepository.findById(Long.valueOf(id));
        return optionalPost.orElse(null);
    }

    public OrderList getCurrentOrder(String id) {
        Optional<OrderList> optionalOrder = orderListRepository.findById(Long.valueOf(id));
        return optionalOrder.orElse(null);
    }

//    public List<ReviewImgUrl> getReviewImgUrlByReview(Review review) { return reviewImgUrlRepository.findAllByReview(review); }

//    public Review getCurrentReview(Long id) {
//        Optional<Review> optionalReview = reviewRepository.findById(id);
//        return optionalReview.orElse(null);
//    }

    public Member getMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }



}
