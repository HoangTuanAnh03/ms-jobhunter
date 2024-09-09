package com.tuananh.authservice.service;

import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.entity.VerificationCode;

public interface VerifyCodeService {

     void delete (VerificationCode verificationCode);

     VerificationCode findByEmail(String email);

     VerificationCode save(VerificationCode verificationCode);

     Boolean isTimeOutRequired(VerificationCode verificationCode, long ms);


     User verify(String code);
}
