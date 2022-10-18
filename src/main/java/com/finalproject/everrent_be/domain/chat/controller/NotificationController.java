package com.finalproject.everrent_be.domain.chat.controller;


import com.finalproject.everrent_be.domain.chat.dto.NotificationDto;
import com.finalproject.everrent_be.domain.chat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notification")
    public List<NotificationDto> getNotification(){
        return notificationService.getNotification();
    }
}