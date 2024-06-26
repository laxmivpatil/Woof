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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.SmsSender;
import com.example.techverse.DTO.OtpVerificationResult;
import com.example.techverse.DTO.RegistrationDTO;
import com.example.techverse.DTO.ResponseDTO;
import com.example.techverse.DTO.UserBasicInfoDTO;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.service.EmailService;
import com.example.techverse.service.NGOService;
import com.example.techverse.service.OtpService;
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
	    private OtpService otpService;
		
	
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

    
    @GetMapping("/user/bytoken")
   	public ResponseEntity<Map<String, Object>> getuserbytoken(@RequestHeader String authorization) {
   		Map<String, Object> responseBody = new HashMap<String, Object>();
    	Optional<User> user=userRepository.findByToken(authorization.substring(7));
   		  
   		 if(user.isEmpty()) {
   		responseBody.put("success", false);
   		responseBody.put("message", "User with specified token not available");
   			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
   		 }
   		 
   		responseBody.put("success", true);
   		responseBody.put("User", new UserBasicInfoDTO(user.get()));
   		
   		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
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
    
    
    
    /****final****/
    @PostMapping("/user/loginbyotp")
	public ResponseEntity<Map<String, Object>> loginuserbyotp(@RequestParam(required = false) String emailorphone, @RequestParam String otp){
    	Map<String, Object> response = new HashMap<String, Object>();
    	 
    	RegistrationDTO  dto=new RegistrationDTO();
		
	    
	    if (StringUtils.isEmpty(emailorphone) || StringUtils.isEmpty(otp)) {
	    	response.put("success", false);;
	        response.put("message","Missing required credentials.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    int otpVerificationResult = otpService.verifyOtp(emailorphone, otp);

	    if (otpVerificationResult == OtpVerificationResult.SUCCESS) {
	        // Generate authentication token (you can use JWT)
	    //    String token = generateToken(phoneNumber);
	    	Optional<User> user=userRepository.findByEmailOrPhone(emailorphone, emailorphone);
	    	user=userService.generateAndSaveToken(user);
			 
	        response.put("success",true);
	        response.put("message","verification successful");
	        response.put("token",user.get().getToken());
	        response.put("profile",user.get().getProfile());
	        response.put("Users", dto.toDTO(user.get()));
	      //  response.setData(token);
	        return ResponseEntity.ok(response);
	    } else if (otpVerificationResult == OtpVerificationResult.EXPIRED) {
	    	 response.put("success",false);
	    	 response.put("message","OTP has expired. Please request a new OTP.");
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    } else {
	    	 response.put("success",false);
	    	 response.put("message","Invalid OTP. Please enter a valid OTP.");
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	}
    @PostMapping("/ngo/loginbyotp")
   	public ResponseEntity<Map<String, Object>> loginngobyotp(@RequestParam(required = false) String emailorphone, @RequestParam String otp){
       	Map<String, Object> response = new HashMap<String, Object>();
       	 
       	RegistrationDTO  dto=new RegistrationDTO();
   		
   	    
   	    if (StringUtils.isEmpty(emailorphone) || StringUtils.isEmpty(otp)) {
   	    	response.put("success", false);;
   	        response.put("message","Missing required credentials.");
   	        return ResponseEntity.badRequest().body(response);
   	    }

   	    int otpVerificationResult = otpService.verifyOtp(emailorphone, otp);

   	    if (otpVerificationResult == OtpVerificationResult.SUCCESS) {
   	        // Generate authentication token (you can use JWT)
   	    //    String token = generateToken(phoneNumber);
   	    	Optional<NGO> ngo=ngoRepository.findByEmailOrPhone(emailorphone, emailorphone);
   	    	ngo=ngoService.generateAndSaveToken(ngo);
   			 
   	        response.put("success",true);
   	        response.put("message","verification successful");
   	        response.put("token",ngo.get().getToken());

	        response.put("profile",ngo.get().getNGOProfile());
   	        response.put("Users", dto.toDTO(ngo.get()));
   	      //  response.setData(token);
   	        return ResponseEntity.ok(response);
   	    } else if (otpVerificationResult == OtpVerificationResult.EXPIRED) {
   	    	 response.put("success",false);
   	    	 response.put("message","OTP has expired. Please request a new OTP.");
   	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
   	    } else {
   	    	 response.put("success",false);
   	    	 response.put("message","Invalid OTP. Please enter a valid OTP.");
   	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
   	    }
   	}
    
    /****final****/
    @PostMapping("/veterinarian/loginbyotp")
	public ResponseEntity<Map<String, Object>> loginveterinarianbyotp(@RequestParam(required = false) String emailorphone, @RequestParam String otp){
    	Map<String, Object> response = new HashMap<String, Object>();
    	 
    	RegistrationDTO  dto=new RegistrationDTO();
		
	    
	    if (StringUtils.isEmpty(emailorphone) || StringUtils.isEmpty(otp)) {
	    	response.put("success", false);;
	        response.put("message","Missing required credentials.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    int otpVerificationResult = otpService.verifyOtp(emailorphone, otp);

	    if (otpVerificationResult == OtpVerificationResult.SUCCESS) {
	        // Generate authentication token (you can use JWT)
	    //    String token = generateToken(phoneNumber);
	    	Optional<Veterinarian> veterinarian=veterinarianRepository.findByEmailOrPhone(emailorphone, emailorphone);
	    	veterinarian=veterinarianService.generateAndSaveToken(veterinarian);
	        response.put("success",true);
	        response.put("message","verification successful");
	        response.put("token",veterinarian.get().getToken());

	        response.put("profile",veterinarian.get().getVeterinarianProfile());
	   	      
	        response.put("Users", dto.toDTO(veterinarian.get()));
	        
	      //  response.setData(token);
	        return ResponseEntity.ok(response);
	    } else if (otpVerificationResult == OtpVerificationResult.EXPIRED) {
	    	 response.put("success",false);
	    	 response.put("message","OTP has expired. Please request a new OTP.");
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    } else {
	    	 response.put("success",false);
	    	 response.put("message","Invalid OTP. Please enter a valid OTP.");
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	}
    
    
    
    
     
	
    private String generateOTP() {
    	final String otp = new Random().ints(1000, 9999).findFirst().orElse(0) + "";

		 
       return String.valueOf(otp);
   }
    
   
    

}
