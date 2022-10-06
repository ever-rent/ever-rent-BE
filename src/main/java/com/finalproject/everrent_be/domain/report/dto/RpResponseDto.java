package com.finalproject.everrent_be.domain.report.dto;

import com.finalproject.everrent_be.domain.report.model.RpUser;
import com.finalproject.everrent_be.domain.report.model.RpPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpResponseDto {
    private String rpreason;
    private Long rpPostuser;
    public RpResponseDto(RpPost rpPost){
        this.rpPostuser=rpPost.getRpproduct().getMember().getId();
        this.rpreason=rpPost.getRpreason();
    }
    public RpResponseDto(RpUser rpUser){
        this.rpreason= rpUser.getRpreason();
    }
}
