package com.tuananh.notificationservice.controller;


import com.tuananh.event.NotificationEvent;
import com.tuananh.notificationservice.dto.request.Recipient;
import com.tuananh.notificationservice.dto.request.SendEmailRequest;
import com.tuananh.notificationservice.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;

    @Value("${api.verify-register}")
    @NonFinal
    String urlVerify;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent message){
        log.info("Message received: {}", message);

        String url = urlVerify + message.getParam().get("code");

        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder()
                        .email(message.getRecipient())
                        .build())
                .subject(message.getSubject())
                .htmlContent("<h1>" + message.getBody() + "</h1>" + "<a href=\""+ url +"\">Confirm Account</a>")
                .build());
    }
}
