package com.tuananh.notificationservice.controller;


//import com.tuananh.notificationservice.dto.ApiResponse;
//import com.tuananh.notificationservice.dto.request.SendEmailRequest;
//import com.tuananh.notificationservice.dto.response.EmailResponse;
//import com.tuananh.notificationservice.service.EmailService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class EmailController {
//    EmailService emailService;
//
//    @PostMapping("/email/send")
//    ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest request){
//        return ApiResponse.<EmailResponse>builder()
//                .data(emailService.sendEmail(request))
//                .build();
//    }
//}
