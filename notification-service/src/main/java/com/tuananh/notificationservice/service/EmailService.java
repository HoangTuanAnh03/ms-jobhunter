package com.tuananh.notificationservice.service;

import com.tuananh.notificationservice.advice.AppException;
import com.tuananh.notificationservice.advice.ErrorCode;
import com.tuananh.notificationservice.dto.request.EmailRequest;
import com.tuananh.notificationservice.dto.request.SendEmailRequest;
import com.tuananh.notificationservice.dto.request.Sender;
import com.tuananh.notificationservice.dto.response.EmailResponse;
import com.tuananh.notificationservice.service.client.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${brevo.api-key}")
    @NonFinal
    String apiKey;

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Hoàng Tuấn Anh")
                        .email("hoangtuananh1772003@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}