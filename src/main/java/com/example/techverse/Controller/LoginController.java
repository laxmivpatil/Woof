package com.example.techverse.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.SmsSender;
import com.example.techverse.DTO.UserBasicInfoDTO;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.service.EmailService;
import com.example.techverse.service.NGOService;
import com.example.techverse.service.UserService;
import com.example.techverse.service.VeterinarianService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


@RestController

public class LoginController {
	
	@Autowired
	private UserService userService;
	
	
	private final LoadingCache<Long, String> otpCache;
    

    @Autowired
     private UserRepository userRepository;
    @Autowired
	private VeterinarianRepository veterinarianRepository;
	
	@Autowired
	private VeterinarianService veterinarianService;
	
	@Autowired
	private NGOService ngoService;
	@Autowired
	private NGORepository ngoRepository;
	
    @Autowired
     EmailService emailService;


    @Autowired
    public LoginController() {
    	System.out.println("gffffffffffffh");
         otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, String>() {
                    @Override
                    public String load(Long key) {
                    System.out.println(key);
                        return generateOTP();
                    }
                });
    }

    
    @PutMapping("/user/bytoken")
   	public ResponseEntity<Map<String, Object>> getuserbytoken(@RequestHeader String authorization) {
   		Map<String, Object> responseBody = new HashMap<String, Object>();
    	Optional<User> user=userRepository.findByToken(authorization.substring(7));
   		 
   		 if(user.isEmpty()) {
   		responseBody.put("success", false);
   		responseBody.put("message", "User with specified token not available");
   			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
   		 }
   		 
   		responseBody.put("success", true);
   		responseBody.put("User", new UserBasicInfoDTO(user.get()));
   		
   		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
   	}
    
    @PutMapping("/user/loginbypwd")
	public ResponseEntity<Map<String, Object>> loginUserByPassword(@RequestParam String emailorphone,String password) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		System.out.println(emailorphone);
		Optional<User> user=userRepository.findByEmailOrPhone(emailorphone, emailorphone);
		 
		 if(user.isEmpty()) {
		responseBody.put("success", false);
		responseBody.put("message", "User with specified email or phone not available");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
		 }

		 return userService.loginUserByPassword(user,password);
	}
    @PutMapping("/veterinarian/loginbypwd")
	public ResponseEntity<Map<String, Object>> loginVeternarianByPassword(@RequestParam String emailorphone,String password) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
 
		Optional<Veterinarian> veterinarian=veterinarianRepository.findByEmailOrPhone(emailorphone, emailorphone);
		 if(veterinarian.isEmpty()) {
		responseBody.put("success", false);
		responseBody.put("message", "Veterinarian with specified email or phone not available");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
		 }

		 return veterinarianService.loginVeterinarianByPassword(veterinarian,password);
	}
    @PutMapping("/ngo/loginbypwd")
	public ResponseEntity<Map<String, Object>> loginNgoByPassword(@RequestParam String emailorphone,String password) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
 
		Optional<NGO> ngo=ngoRepository.findByEmailOrPhone(emailorphone, emailorphone);
		 if(ngo.isEmpty()) {
		responseBody.put("success", false);
		responseBody.put("message", "Ngo with specified email or phone not available");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
		 }

		 return ngoService.loginNgoByPassword(ngo,password);
	}
    
    @PostMapping("/loginbyotp/final")
    public ResponseEntity<Map<String, Object>> loginbyOtp( @RequestParam(required = false) String emailorphone, @RequestParam String otp) {
        Optional<User> user = Optional.empty();
       if(emailorphone != null && !emailorphone.isEmpty()) {
            user = userRepository.findByEmailOrPhone(emailorphone,emailorphone);
        }

        if (user.isPresent()) {
            Long userId = user.get().getId();
            String cachedOtp = otpCache.getIfPresent(userId);
            if (cachedOtp != null && cachedOtp.equals(otp)) {
                Map<String, Object> responseBody = new HashMap<>();
               user=userService.generateAndSaveToken(user);
                System.out.println("token "+user.get().getToken());
			
                responseBody.put("success", true);
                responseBody.put("message", "Login Successfull");
                responseBody.put("token ",user.get().getToken());
                responseBody.put("userId",user.get().getId());
                
                
                return ResponseEntity.ok(responseBody);
            } else {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("success", false);
                responseBody.put("message", "Invalid OTP");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
            }
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
