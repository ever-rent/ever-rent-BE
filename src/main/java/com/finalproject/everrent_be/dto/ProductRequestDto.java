package com.finalproject.everrent_be.dto;

import com.finalproject.everrent_be.model.Authority;
import com.finalproject.everrent_be.model.Member;
import com.finalproject.everrent_be.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {

    private String productName;
    private String price;
    private String content;
    private String cateId;
    private String rentStart;
    private String rentEnd;

    private String location;

}
