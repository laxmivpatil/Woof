package com.example.techverse.service;
 


 
import com.example.techverse.DTO.OtpVerificationResult;
import com.example.techverse.Model.OtpEntity;
import com.example.techverse.Repository.OtpRepository;
import com.twilio.Twilio;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

	 @Autowired
	    private OtpRepository otpRepository;
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	    
   // @Value("${twilio.accountSid}")
    private String accountSid="AC5f95b80c20f80833cbdb2faae5debc59";

   // @Value("${twilio.authToken}")
    private String authToken="a6f4c99b490c5f85620fe49140cf7cde";

   // @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber="+12139854075";

  //  @Value("${twilio.phone-number}")
    private String twilioWhatsappNumber="+14155238886";
   
    public String generateOtp() {
        // Generate a 6-digit random OTP
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }
 //    please remove this comment to send sms on mobile and comment above simple sendotp method
    public boolean sendOtp(String phoneNumber, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        String messageBody = "Your OTP is: " + otp;
        String  phoneNumber1 = "+91" + phoneNumber;

             System.out.println("OTP sent successfully! SID: " +otp);
                OtpEntity otpEntity = new OtpEntity(phoneNumber, passwordEncoder.encode(otp), expiryTime);
                otpRepository.save(otpEntity);
                return true;
           
         
    }
 
 /*   public boolean sendOtp(String phoneNumber, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        String messageBody = "Your OTP is: " + otp;
        Twilio.init(accountSid, authToken);
       String  phoneNumber1 = "+91" + phoneNumber;

        try {
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(phoneNumber1),
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                    messageBody)
                    .create();

            // Check if the message was sent successfully
            
            if (message.getSid()!=null) {
                System.out.println("OTP sent successfully! SID: " +  passwordEncoder.encode(otp));
                OtpEntity otpEntity = new OtpEntity(phoneNumber,  passwordEncoder.encode(otp), expiryTime);
                otpRepository.save(otpEntity);
                return true;
            } else {
                System.out.println("Failed to send OTP!");
                return false;
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., TwilioException)
            System.out.println("Error occurred while sending OTP: " + e.getMessage());
            return false;
        }
    }
    */
    public int verifyOtp(String phoneNumber, String otp) {
        Optional<OtpEntity> otpEntityOptional = otpRepository.findByPhoneNumber(phoneNumber);

        if (otpEntityOptional.isPresent() && passwordEncoder.matches(otp,otpEntityOptional.get().getOtp())) {
            OtpEntity otpEntity = otpEntityOptional.get();
            if (otpEntity.getExpiryTime().isAfter(LocalDateTime.now())) {
                // OTP is valid and not expired
                // Optionally, you can remove the used OTP from the database to prevent replay attacks.
                otpRepository.delete(otpEntity);
                return OtpVerificationResult.SUCCESS;
            } else {
                // OTP has expired
                return OtpVerificationResult.EXPIRED;
            }
        }

        // OTP verification failed
        return OtpVerificationResult.INVALID;
    }     
    
    
      
      
}
