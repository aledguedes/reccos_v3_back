package com.aledguedes.reccos_v3_back.repository;

import com.aledguedes.reccos_v3_back.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
    Optional<VerificationCode> findByEmailAndCodeAndUsedFalse(String email, String code);

    Optional<VerificationCode> findByEmailAndUsedTrue(String email);
}