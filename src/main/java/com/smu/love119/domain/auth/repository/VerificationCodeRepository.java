package com.smu.love119.domain.auth.repository;

import com.smu.love119.domain.auth.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByUsername(String username);

    Optional<VerificationCode> findTopByUsernameOrderByExpirationTimeDesc(String username);

}