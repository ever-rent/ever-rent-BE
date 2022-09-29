package com.finalproject.everrent_be.model;

import com.finalproject.everrent_be.dto.ProductRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
public class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String price;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: 참조 객체들의 데이터들은 무시하고 해당 엔티티의 데이터만을 가져옴
    private Member member;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderList> orderLists;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String cateId;
    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private LocalDate rentStart;

    @Column(nullable = false)
    private LocalDate rentEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;




    public void update(ProductRequestDto productRequestDto,Member member, String bucket){
        this.productName=productRequestDto.getProductName();
        this.price=productRequestDto.getPrice();
        this.content=productRequestDto.getContent();
        this.imgUrl=bucket;
        this.cateId=productRequestDto.getCateId();
        this.rentStart=StrToLocalDate(productRequestDto.getRentStart());
        this.rentEnd=StrToLocalDate(productRequestDto.getRentEnd());
        this.status=Status.WAITING;
        this.member=member;
    }
    public void updateStatus(Status status)
    {
        this.status=status;
    }

    public LocalDate StrToLocalDate(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(string,formatter);
        return date;
    }
    public String LocalDateToStr(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
