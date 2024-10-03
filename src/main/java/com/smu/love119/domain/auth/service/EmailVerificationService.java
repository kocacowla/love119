package com.smu.love119.domain.auth.service;

import com.smu.love119.domain.auth.entity.VerificationCode;
import com.smu.love119.domain.auth.repository.VerificationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailVerificationService {

    private final EmailSender emailSender; // 이메일 발송을 위한 서비스 (또는 다른 방법으로 발송)
    private final VerificationCodeRepository verificationCodeRepository;

    public EmailVerificationService(EmailSender emailSender, VerificationCodeRepository verificationCodeRepository) {
        this.emailSender = emailSender;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    // username으로 인증번호 발송
    public void sendVerificationCode(String username) {
        String verificationCode = generateVerificationCode(); // 랜덤 인증번호 생성
        saveVerificationCode(username, verificationCode); // 인증번호 저장
        emailSender.sendEmail(username, "인증번호", "인증번호: " + verificationCode); // 인증번호 발송 (username을 이메일로 가정)
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(999999)); // 6자리 인증번호 생성
    }

    private void saveVerificationCode(String username, String verificationCode) {
        VerificationCode code = new VerificationCode(username, verificationCode, LocalDateTime.now().plusMinutes(10)); // 만료 시간 10분 설정
        verificationCodeRepository.save(code);
    }

    // 인증번호 확인
    public boolean verifyCode(String username, String code) {
        Optional<VerificationCode> verificationCodeOpt = verificationCodeRepository.findTopByUsernameOrderByExpirationTimeDesc(username);

        if (verificationCodeOpt.isPresent()) {
            VerificationCode verificationCode = verificationCodeOpt.get();

            // 인증 코드가 만료되었는지 확인
            if (verificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("인증 코드가 만료되었습니다.");
            }

            // 인증 코드 일치 여부 확인
            if (verificationCode.getCode().equals(code)) {
                return true;
            } else {
                throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
            }
        }

        throw new IllegalArgumentException("해당 사용자의 인증 코드를 찾을 수 없습니다.");
    }
}