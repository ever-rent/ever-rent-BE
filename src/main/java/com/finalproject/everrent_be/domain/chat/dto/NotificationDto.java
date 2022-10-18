package com.finalproject.everrent_be.domain.chat.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {
    String message;

    String memberName;

    LocalDateTime createdAt;

    String roomId;
}