package com.tuananh.authservice.repository;

import com.tuananh.authservice.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VerifyCodeRepository extends JpaRepository<VerificationCode, String> {
    boolean existsByEmail(String email);

    Optional<VerificationCode> findFirstByCode(String code);

    Optional<VerificationCode> findFirstByEmail(String email);
}
