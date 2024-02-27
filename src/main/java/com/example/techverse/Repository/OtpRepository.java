package com.example.techverse.Repository;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.OtpEntity;
 

public interface OtpRepository extends JpaRepository<OtpEntity, String> {

    Optional<OtpEntity> findByPhoneNumberAndOtpAndExpiryTimeAfter(String phoneNumber, String otp, LocalDateTime expiryTime);

    Optional<OtpEntity> findByPhoneNumberAndOtp(String phoneNumber, String otp);
    Optional<OtpEntity> findByPhoneNumber(String phoneNumber );

}