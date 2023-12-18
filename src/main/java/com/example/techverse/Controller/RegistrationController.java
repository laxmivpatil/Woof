package com.example.techverse.Controller;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.SmsSender;

import com.example.techverse.Model.User;

import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.FieldValidationException;
import com.example.techverse.exception.UserAlreadyExistsException;
import com.example.techverse.service.EmailService;
import com.example.techverse.service.UserService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.apache.commons.validator.routines.EmailValidator;

@RestController
public class RegistrationController {

	 
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	 private final LoadingCache<Long, String> otpCache;
	 
	 @Autowired
	    public RegistrationController() {
	         otpCache = CacheBuilder.newBuilder()
	                .expireAfterWrite(1, TimeUnit.MINUTES)
	                .build(new CacheLoader<Long, String>() {
	                    @Override
	                    public String load(Long key) {
	                    	Random rand = new Random(); 
	               		 final  String otp= new Random().ints(1, 100000, 999999).sum()+"";
	                        return String.valueOf(otp);
	                    }
	                });
	    }

	// New User Registration
	 @GetMapping("/")
	 public String welcome()
	 {
		 return "Welcome";
	 }
	 
	 /*
	  * . Email Id
2. Phone Number
3. Password / OTP
4. Full Name
5. Confirm Password
	  * 
	  * 
	  * 
	  */
	 
	 
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> signUpUser(@RequestParam String email,String phone ,String password,String fullname,String confirmPassword) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		try {
			// Validate the Request Body
		 
			validateRegistrationRequest(email,phone,password,fullname,confirmPassword);
			User user=new User();
			user.setPhone(phone); 
			Random rand = new Random();
			int otp = rand.nextInt(9999);
			System.out.println("verification code send");
			sendVerificationCodetoPhone(user,otp+"");
				// Check if the string contains an email address
			System.out.println("verification code send");
			 user.setEmail(email);
			 sendVerificationCodetoEmail(user,otp);
			user.setFullName(fullname);
			String hashedPassword =passwordEncoder.encode(password);
			 List<String> lastThreePasswords = user.getLastThreePasswords();
			 if (lastThreePasswords == null) {
			        lastThreePasswords = new ArrayList<>();
			    }
			    lastThreePasswords.add(hashedPassword);
			    if (lastThreePasswords.size() > 3) {
			        lastThreePasswords.remove(0); // Remove the oldest password
			    }

			    user.setLastThreePasswords(lastThreePasswords);
			    // Store the hashed password in the user object or database
			user.setPassword(hashedPassword);
			System.out.println("Hashed Password   "+hashedPassword);
			//user.setPassword(password);
			Optional<User> userOptional = Optional.ofNullable(userService.registerUser(user));

			responseBody.put("success", true);
			responseBody.put("message", "UserRegistration Successfull");
			responseBody.put("user_id", userOptional.get().getId());
			responseBody.put("verification_status", "Pending");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);

		} catch (UserAlreadyExistsException e) {
			responseBody.put("success", false);
			responseBody.put("message", e.getMessage());

			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.CONFLICT);

			// return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (FieldValidationException e) {

			responseBody.put("success", false);
			responseBody.put("message", e.getMessage());

			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.BAD_REQUEST);
			// return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			System.out.println(" registration");

			responseBody.put("success", false);
			responseBody.put("message", "An error Occured");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	//authenticate user with otp
	@PutMapping("/authenticate")
	public ResponseEntity<Map<String, Object>> authenticate(@RequestParam String otp,String emailorphone) {
		Map<String, Object> responseBody = new HashMap<String, Object>();

		System.out.println("Authentication");
		try {
			// Validate the Request Body
			System.out.println("hi welcome to registration");
			Optional<User> user=userRepository.findByEmailOrPhone(emailorphone,emailorphone);
			
			if(user.isEmpty())
			{
				responseBody.put("success", false);
				responseBody.put("message", "User with specified email or phone not available");
				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
		
			}
			System.out.println(user.get());
			System.out.println("hgfhhjgfhgdfhgh");
			String otp1=user.get().getOtp();
			if(!otp1.equals(otp)) {

				responseBody.put("success", false);
				responseBody.put("message", "Invalid Otp");
				responseBody.put("verification_status", "pending");
				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
			} 

			user.get().setVerification("Verified");
			User user1= userRepository.save(user.get());
			responseBody.put("success", true);
			responseBody.put("message", "UserRegistration Successfull");
			responseBody.put("user_id", user.get().getId());
			responseBody.put("verification_status", "verified");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);

		} 
		catch (Exception e) {
			System.out.println(" registration");

			responseBody.put("success", false);
			responseBody.put("message", "An error Occured");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	//update user after registration and verification
	@PutMapping("/update")
	public ResponseEntity<Map<String, Object>> update(@RequestParam String emailorphone,String role,Double latitude,Double longitude) {
		Map<String, Object> responseBody = new HashMap<String, Object>();

		System.out.println("Authentication");
		try {
			// Validate the Request Body
			System.out.println("hi welcome to registration");
			Optional<User> user=userRepository.findByEmailOrPhone(emailorphone,emailorphone);
			if(user.isEmpty()) {
				responseBody.put("success", false);
				responseBody.put("message", "User with specified email or phone not available");
				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
			}			if(!user.get().getVerification().equals("Verified")) {

				responseBody.put("success", false);
				responseBody.put("message", "User Not Verified");


				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
			}
			user.get().setRole(role);
			user.get().setVerification("Verified");
			user.get().setLatitude(latitude);
			user.get().setLongitude(longitude);
			User user1= userRepository.save(user.get());

			responseBody.put("success", true);
			responseBody.put("message", "User Updated Successfull");
			responseBody.put("user_id", user.get().getId());
			responseBody.put("verification_status", "verified");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);

		} 
		catch (Exception e) {
			System.out.println(" registration");

			responseBody.put("success", false);
			responseBody.put("message", "An error Occured");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	public void validateRegistrationRequest(String email,String phone ,String password,String fullname,String confirmPassword) {
		String message = "";     
		// Regular expression pattern for a mobile number
		String mobilePattern = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";

		// Regular expression pattern for an email address
		String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";

		// Create the patterns
		Pattern mobileRegex = Pattern.compile(mobilePattern);
		Pattern emailRegex = Pattern.compile(emailPattern);

		// Check if the string contains a mobile number
		Matcher mobileMatcher = mobileRegex.matcher(phone);
		Matcher emailMatcher = emailRegex.matcher(email);

		if (!mobileMatcher.find()) {
			System.out.println("Mobile number found.");
			message = message + " Phone no not valid ";

		}

		if (!emailMatcher.find()) {
			message = message + " Email not valid ";
		}
		if (StringUtils.isBlank(password)
				|| !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,12}$")) {
			message = message+ "Password must contain ";
			// "1 Capital Letter,1 Small Letter, 1 Special Symbol and maximun 12 character
			// and min 8";
			String pwd = password;

			if (!pwd.matches(".*[A-Z].*"))
				message = message + ",1 Capital Letter";
			if (!pwd.matches(".*[a-z].*"))
				message = message + ", 1 small Letter";
			if (!pwd.matches(".*[0-9].*"))
				message = message + ", 1 numeric";
			if (!pwd.matches(".*[@#$%^&+=].*"))
				message = message + ", 1 Special character";
			if (pwd.length() < 8 || pwd.length() > 12)
				message = message + " Minimum 8 character and maximum 12 character size";

		}
		
		if(!password.equals(confirmPassword))
		{
			message = message + " Password and Confirm password not match";
		}
		if (!message.equals("")) {
			System.out.println("hobnbnb verification");
			throw new FieldValidationException(message);
		}
	}


	public void sendVerificationCodetoEmail(User user,int otp) {
		System.out.println("User not avbl");

	//	remove comment after getting an otp
		 	emailService.sendEmail(user.getEmail(), otp);
		user.setOtp(otp + "");
		user.setVerification("pending");
		System.out.println("verification code send");
	}
	public void sendVerificationCodetoPhone(User user,String otp) {
		System.out.println("User not avbl");
		// sms send logic
//		remove comment after getting an otp
		 SmsSender.smsSent("+91" + user.getPhone(), otp + "");
		user.setOtp(otp + "");
		user.setVerification("pending");
	}


}
