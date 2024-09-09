package com.tuananh.authservice.service.impl;

import com.tuananh.authservice.advice.AppException;
import com.tuananh.authservice.advice.ErrorCode;
import com.tuananh.authservice.advice.exception.ResourceNotFoundException;
import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.entity.VerificationCode;
import com.tuananh.authservice.repository.UserRepository;
import com.tuananh.authservice.repository.VerifyCodeRepository;
import com.tuananh.authservice.service.VerifyCodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerifyCodeServiceImpl implements VerifyCodeService {
    VerifyCodeRepository verifyCodeRepository;
    UserRepository userRepository;

    @NonFinal
    @Value("${auth.verify.code-duration}")
    protected long CODE_DURATION;

    /**
     * @param verificationCode
     * @return
     */
    @Override
    public void delete(VerificationCode verificationCode) {
        verifyCodeRepository.delete(verificationCode);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public VerificationCode findByEmail(String email) {
         return verifyCodeRepository.findFirstByEmail(email).orElse(null);
    }

    /**
     * @param verificationCode
     * @return
     */
    @Override
    public VerificationCode save(VerificationCode verificationCode) {
        return verifyCodeRepository.save(verificationCode);
    }

    /**
     * @param verificationCode
     * @param ms
     * @return
     */
    @Override
    public Boolean isTimeOutRequired(VerificationCode verificationCode, long ms) {
        if (verificationCode.getCode() == null) {
            return true;
        }

        long currentTimeInMillis =LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long otpRequestedTimeInMillis = verificationCode.getExp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return otpRequestedTimeInMillis + ms <= currentTimeInMillis;
    }

    /**
     * @param code
     */
    @Override
    public User verify(String code) {
        VerificationCode verificationCode = verifyCodeRepository.findFirstByCode(code).orElseThrow(
            () -> new AppException(ErrorCode.VERIFY_FAILED)
        );

        if (isTimeOutRequired(verificationCode, CODE_DURATION)) throw new AppException(ErrorCode.VERIFY_EXPIRED);

        User userByEmail = userRepository.findByEmail(verificationCode.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", verificationCode.getEmail())
        );

        if (userByEmail.getActive()) throw new AppException(ErrorCode.VERIFY_FAILED);

        userByEmail.setActive(true);
        userRepository.save(userByEmail);

        return userByEmail;
    }
}
