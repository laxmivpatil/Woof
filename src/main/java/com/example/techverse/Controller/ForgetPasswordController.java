package com.example.techverse.Controller;

import com.example.techverse.SmsSender;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.service.EmailService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/forgetpassword")
public class ForgetPasswordController {

    private final LoadingCache<Long, String> otpCache;
    @Autowired
	private BCryptPasswordEncoder passwordEncoder;

    @Autowired
     private UserRepository userRepository;
    @Autowired
     EmailService emailService;

 
    public ForgetPasswordController() {
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, String>() {
                    @Override
                    public String load(Long key) {
                        return generateOTP();
                    }
                });
    }
    
    
    @GetMapping("/find")
    public ResponseEntity<Map<String, Object>> findUser(@RequestParam String emailorphone) {
    	System.out.println("otp==>"+otpCache);
        Optional<User> user = Optional.empty();
        if (emailorphone != null && !emailorphone.isEmpty()) {
            user = userRepository.findByEmailOrPhone(emailorphone,emailorphone);
        }

        if (!user.isEmpty()) {
            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Users List ");
            responseBody.put("user", user.get());
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", false);
            responseBody.put("message", "User not found with that email or phone");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    @GetMapping("/getotp")
    public ResponseEntity<Map<String, Object>> generateOtp(@RequestParam String emailorphone) {
    	System.out.println("otp==>"+otpCache);
        Optional<User> user = Optional.empty();
        if (emailorphone != null && !emailorphone.isEmpty()) {
            user = userRepository.findByEmailOrPhone(emailorphone,emailorphone);
        }

        if (!user.isEmpty()) {
            Long userId = user.get().getId();
            String otp = otpCache.getUnchecked(userId);
            //emailService.sendEmail(user.get().getEmail(),"Verification OTP  ","OTP is "+otp);
			//SmsSender.smsSent("+91" + user.get().getPhone(), otp + "");
			

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "OTP generated successfully "+otp);
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", false);
            responseBody.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    @PostMapping("/verifyotp")
    public ResponseEntity<Map<String, Object>> validateOtp( @RequestParam(required = false) String emailorphone, @RequestParam String otp) {
        Optional<User> user = Optional.empty();
       if(emailorphone != null && !emailorphone.isEmpty()) {
            user = userRepository.findByEmailOrPhone(emailorphone,emailorphone);
        }

        if (user.isPresent()) {
            Long userId = user.get().getId();
            String cachedOtp = otpCache.getIfPresent(userId);
            if (cachedOtp != null && cachedOtp.equals(otp)) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("success", true);
                responseBody.put("message", "OTP validation successful");
                return ResponseEntity.ok(responseBody);
            } else {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("success", false);
                responseBody.put("message", "Invalid OTP");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", false);
            responseBody.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    @PostMapping("/final")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam(required = false) String emailorphone, @RequestParam String password) {
        Optional<User> user = Optional.empty();
         if (emailorphone != null && !emailorphone.isEmpty()) {
            user = userRepository.findByEmailOrPhone(emailorphone,emailorphone);
        }

        if (user.isPresent()) {
            User existingUser = user.get();
            System.out.println("New Password :-"+password);
            
            String hashedPassword =passwordEncoder.encode(password);
            List<String> lastThreePasswords = existingUser.getLastThreePasswords();
            if (lastThreePasswords != null && lastThreePasswords.stream().anyMatch(p -> passwordEncoder.matches(password, p))) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("success", false);
                responseBody.put("message", "New password should not match the last three passwords");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }
            String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
            if (!password.matches(passwordPattern)) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("success", false);
                responseBody.put("message", "Password does not meet the requirements");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }
            
            
            existingUser.setPassword(hashedPassword);
            if (lastThreePasswords == null) {
                lastThreePasswords = new ArrayList<>();
            }
            lastThreePasswords.add(hashedPassword);
            if (lastThreePasswords.size() > 3) {
                lastThreePasswords.remove(0); // Remove the oldest password
            }
            existingUser.setLastThreePasswords(lastThreePasswords);

            userRepository.save(existingUser);
            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Password reset successful");
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", false);
            responseBody.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    private String generateOTP() {
    	final String otp = new Random().ints(1000, 9999).findFirst().orElse(0) + "";
       return String.valueOf(otp);
   }
    
   
    
    
    
 
}

