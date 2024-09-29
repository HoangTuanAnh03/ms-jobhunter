package com.tuananh.authservice.service;

import com.tuananh.authservice.dto.request.VerifyNewPasswordRequest;
import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.entity.VerificationCode;

public interface VerifyCodeService {

     void delete (VerificationCode verificationCode);

     VerificationCode findByEmail(String email);

     VerificationCode save(VerificationCode verificationCode);

     Boolean isTimeOutRequired(VerificationCode verificationCode, long ms);

     User verifyRegister(String code);

     User verifyForgotPassword(VerifyNewPasswordRequest request);
}
