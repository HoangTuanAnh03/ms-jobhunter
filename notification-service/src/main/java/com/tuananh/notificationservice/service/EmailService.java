package com.tuananh.notificationservice.service;

import com.tuananh.event.NotificationEvent;
import com.tuananh.notificationservice.advice.AppException;
import com.tuananh.notificationservice.advice.ErrorCode;
import com.tuananh.notificationservice.dto.request.EmailRequest;
import com.tuananh.notificationservice.dto.request.Recipient;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;
    private SpringTemplateEngine templateEngine;

    @Value("${brevo.api-key}")
    @NonFinal
    String apiKey;

    @Value("${api.verify-register}")
    @NonFinal
    String urlVerifyRegister;

    @Value("${api.verify-forgot-password}")
    @NonFinal
    String urlVerifyForgotPassword;

    public EmailResponse sendEmailRegister(NotificationEvent message) {
        String html = templateEngine.process("verify_register", getContext(message, urlVerifyRegister));

        return send(message, html);
    }

    public EmailResponse sendEmailForgotPassword(NotificationEvent message) {
        String html = templateEngine.process("verify_forgot_password",  getContext(message, urlVerifyForgotPassword));

        return send(message, html);
    }

    private Context getContext(NotificationEvent message, String urlVerifyForgotPassword) {
        Map<String, Object> props = new HashMap<>();
        String url = urlVerifyForgotPassword + message.getParam().get("code");

        props.put("name", message.getParam().get("name"));
        props.put("url", url);

        Context context = new Context();
        context.setVariables(props);
        return context;
    }

    private EmailResponse send(NotificationEvent message, String html){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Hoàng Tuấn Anh")
                        .email("hoangtuananh1772003@gmail.com")
                        .build())
                .to(List.of(Recipient.builder()
                        .email(message.getRecipient())
                        .build()))
                .subject(message.getSubject())
                .htmlContent(html)
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
