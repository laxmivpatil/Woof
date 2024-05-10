package com.example.techverse.Controller;
 
import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.DistanceCalculator;
import com.example.techverse.JwtUtil;
import com.example.techverse.SmsSender;
import com.example.techverse.DTO.AnimalRescueRequestDTO;
import com.example.techverse.DTO.RegistrationDTO;
import com.example.techverse.DTO.UserBasicInfoDTO;
import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.exception.FieldValidationException;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.exception.UserAlreadyExistsException;
import com.example.techverse.service.EmailService;
import com.example.techverse.service.NGOService;
import com.example.techverse.service.StorageService;
import com.example.techverse.service.UserService;
import com.example.techverse.service.VeterinarianService;
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
		private StorageService storageService;
	 
	@Autowired
	private VeterinarianRepository veterinarianRepository;
	
	@Autowired
	private VeterinarianService veterinarianService;
	
	@Autowired
	private NGOService ngoService;
	@Autowired
	private NGORepository ngoRepository;
	
	@Autowired
	private JwtUtil jwtutil;
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
1. Email Id
2. Phone Number
3. Password / OTP
4. Full Name
5. Confirm Password
*/
	 
	 @GetMapping("/checkusersbyphoneoremail")
		public ResponseEntity<Map<String, Object>> checkUser(@RequestParam String emailorphone ) {
			Map<String, Object> responseBody = new HashMap<String, Object>();
			try {
				// Validate the Request Body
				RegistrationDTO  dto=new RegistrationDTO();
				Optional<User> user=userRepository.findByEmailOrPhone(emailorphone,emailorphone);
				Optional<Veterinarian> veterinarian=veterinarianRepository.findByPhoneOrEmail(emailorphone);
				Optional<NGO> ngo=ngoRepository.findByPhoneOrEmail(emailorphone);
				
				if(user.isPresent())
				{
					 responseBody.put("success", false);
					 responseBody.put("message", "This email or phone allready registered ");
				 responseBody.put("Users", dto.toDTO(user.get())); 
				 		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
				}
				if(veterinarian.isPresent())
				{
					 responseBody.put("success", false);
					 responseBody.put("message", "This email or phone allready registered ");
					  responseBody.put("Users",dto.toDTO(veterinarian.get()));
					 	return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
				}
				if(ngo.isPresent())
				{
					 responseBody.put("success", false);
					 responseBody.put("message", "This email or phone allready registered ");
					  responseBody.put("Users", dto.toDTO(ngo.get()));
						return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
				}
				 
				 
				responseBody.put("success", true);
				responseBody.put("message", "new user");
		 		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);

			 		} catch (Exception e) {
				 

				responseBody.put("success", false);
				responseBody.put("message", "An error Occured"+e);
				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	 
	 /// 1.checkuserbymobile  2generateotp-mobileno,  3. user/loginbyotp    mobileno ,otp  veterniarian/loginbyotp  ngo/loginbyotp
	 	//											1. user/loginbypwd   email pwd
/*	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> signUpUser(@RequestParam String email,String phone ,String password,String fullname,String confirmPassword,String role) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		try {
			// Validate the Request Body
		 
			//validateRegistrationRequest(email,phone,password,fullname,confirmPassword);
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
			responseBody.put("message", "UserRegistration Successfull Otp=>"+otp);
			responseBody.put("user_id", userOptional.get().getId());
			responseBody.put("Otp",otp);
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
*/ 
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> signUpUser(
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) String phone,
	        @RequestParam String password,
	        @RequestParam String fullname,
	        @RequestParam String confirmPassword,
	        @RequestParam String role) {
		Map<String, Object> responseBody = new HashMap<String, Object>();
		 RegistrationDTO  dto=new RegistrationDTO();
		try {
			// Validate the Request Body
			Long entity_id=0L;
			String token="";
			 
			// validateRegistrationRequest(email,phone,password,fullname,confirmPassword);
			 if(role.equals("User")) {
				  System.out.println(email);
				  User user = userService.registerUser(email,phone,password,fullname,role);
				entity_id=user.getId();
				user=userService.generateAndSaveToken1(user);
				token=user.getToken();
				responseBody.put("User", dto.toDTO(user));
			 
			 }
			 else if (role.equals("Veterinarian")) {
				 Veterinarian veterinarian = veterinarianService.registerVeterinarian(email,phone,password,fullname,role);
					entity_id=veterinarian.getId();
					veterinarian = veterinarianService.generateAndSaveToken1(veterinarian);
					token=veterinarian.getToken();
					responseBody.put("Veterinarian", dto.toDTO(veterinarian));
						
			 }
			 else if(role.equals("NGO")) {
					NGO ngo =ngoService.registerNGO(email,phone,password,fullname,role);
					entity_id=ngo.getId();
					ngo = ngoService.generateAndSaveToken1(ngo);
					token=ngo.getToken();
					responseBody.put("NGO", dto.toDTO(ngo));
					
			 }		 
		
			
			responseBody.put("success", true);
			 responseBody.put("entity_id", entity_id);
			responseBody.put("role",role);
			responseBody.put("token",token);
			
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
	//no use
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
	
	
	@PutMapping("/user/update")
	public ResponseEntity<Map<String, Object>> userupdate(@RequestHeader("Authorization") String authorizationHeader,
			@RequestPart("fullName") String fullName,
			@RequestPart("phoneNumber") String phoneNumber,
			@RequestPart("email") String email,
			@RequestPart("age") String age,
			@RequestPart("gender") String gender,
			@RequestPart("profile") MultipartFile profile){
			
			Map<String, Object> responseBody = new HashMap<String, Object>();

		System.out.println("Authentication");
		try {
			// Validate the Request Body
			Optional<User> user=userRepository.findByToken(authorizationHeader.substring(7));
		 	
			 
			if(!user.isPresent()) {
				responseBody.put("success", false);
				responseBody.put("message", "User with invalid token");
				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
			}		
			 
			user.get().setFullName(fullName);
			user.get().setVerification("Verified");
			user.get().setPhone(phoneNumber);
			user.get().setAge(Long.parseLong(age));
			user.get().setGender(gender);
			user.get().setEmail(email);
			if(profile!=null && !profile.isEmpty())
			{
				String p=storageService.uploadFileOnAzure(profile);
				user.get().setProfile(p);
			}
			userRepository.save(user.get());

			responseBody.put("success", true);
			responseBody.put("message", "User Updated Successfull");
			responseBody.put("user_id", user.get().getId());
			responseBody.put("token", user.get().getToken());
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
	@PutMapping("/ngo/update")
	public ResponseEntity<Map<String, Object>> ngoupdate(@RequestHeader("Authorization") String authorizationHeader,
			@RequestPart("ngoName") String ngoName,
			@RequestPart("address") String address,
			@RequestPart("phoneNumber") String phoneNumber,
			@RequestPart("email") String email,
			@RequestPart("ngodate") String ngodate,
			@RequestPart("ngoCertificate") MultipartFile ngoCertificate,
			@RequestPart("ngoProfile") MultipartFile ngoProfile
			){
			
			Map<String, Object> responseBody = new HashMap<String, Object>();
			System.out.println("Authentication");
		try {
			// Validate the Request Body
			 	Optional<NGO> ngo=ngoRepository.findByToken(authorizationHeader.substring(7));
			if(ngo.isEmpty()) {
				responseBody.put("success", false);
				responseBody.put("message", "NGO with invalid token");
				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
			}			
			 
			ngo.get().setNgoName(ngoName);
			ngo.get().setVerification("Verified");
			ngo.get().setPhone(phoneNumber);
			ngo.get().setNGODate(ngodate);
			ngo.get().setAddress(address);
			ngo.get().setEmail(email);
			if(ngoProfile!=null && !ngoProfile.isEmpty())
			{
				String p=storageService.uploadFileOnAzure(ngoProfile);
				ngo.get().setNGOProfile(p);
			}
			if(ngoCertificate!=null && !ngoCertificate.isEmpty())
			{
				String p=storageService.uploadFile(ngoCertificate);
				ngo.get().setNGOCertificate(p);
			}
			ngoRepository.save(ngo.get());
			responseBody.put("success", true);
			responseBody.put("message", "User Updated Successfull");
			responseBody.put("user_id", ngo.get().getId());
			responseBody.put("token",ngo.get().getToken());
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
	@PutMapping("/veterinarian/update")
	public ResponseEntity<Map<String, Object>> veterinarianupdate(@RequestHeader("Authorization") String authorizationHeader,
			@RequestPart("fullName") String fullName,
			@RequestPart("address") String address,
			@RequestPart("phoneNumber") String phoneNumber,
			@RequestPart("email") String email,
			@RequestPart("age") String age,
			@RequestPart("experience") String experience,
			@RequestPart("gender") String gender,
			@RequestPart("veterinarianCertificate") String veterinarianCertificate,
			@RequestPart("veterinarianCertification") MultipartFile veterinarianCertification,
			@RequestPart("veterinarianProfile") MultipartFile veterinarianProfile
			){
				Map<String, Object> responseBody = new HashMap<String, Object>();

		System.out.println("Authentication");
		try {
			// Validate the Request Body
		 		Optional<Veterinarian> veterinarian=veterinarianRepository.findByToken(authorizationHeader.substring(7));
			if(veterinarian.isEmpty()) {
				responseBody.put("success", false);
				responseBody.put("message", "User with invalid token");
				return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
			}			 
			veterinarian.get().setFullName(fullName);
			veterinarian.get().setVerification("Verified");
			veterinarian.get().setPhone(phoneNumber);
			veterinarian.get().setEmail(email);
			veterinarian.get().setAge(Long.parseLong(age));
			veterinarian.get().setGender(gender);
			veterinarian.get().setExperience(Long.parseLong(experience));

			veterinarian.get().setVeterinarianCertificate(veterinarianCertificate);
			if(veterinarianCertification!=null && !veterinarianCertification.isEmpty())
			{
				String p=storageService.uploadFileOnAzure(veterinarianCertification);
				veterinarian.get().setVeterinarianCertification(p);
			}
			if(veterinarianProfile!=null && !veterinarianProfile.isEmpty())
			{
				String p=storageService.uploadFileOnAzure(veterinarianProfile);
				veterinarian.get().setVeterinarianProfile(p);
			}
			veterinarianRepository.save(veterinarian.get());

			responseBody.put("success", true);
			responseBody.put("message", "Veterinarian Updated Successfull");
			responseBody.put("user_id", veterinarian.get().getId());
			responseBody.put("token", veterinarian.get().getToken());
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
		 	emailService.sendEmail(user.getEmail(), otp+"");
		user.setOtp(otp + "");
		user.setVerification("pending");
		System.out.println("verification code send");
	}
	public void sendVerificationCodetoPhone(User user,String otp) {
		System.out.println("User not avbl");
		// sms send logic
//		remove comment after getting an otp
//		 SmsSender.smsSent("+91" + user.getPhone(), otp + "");
		user.setOtp(otp + "");
		user.setVerification("pending");
	}
	@GetMapping("/info/{entityType}/{entityId}")
	public ResponseEntity<Map<String,Object>> getUserRescueRequests(
			@RequestHeader("Authorization") String accessToken,@PathVariable String entityType, @PathVariable Long entityId
			 ) throws IOException, UnauthorizedAccessException {
		// Find the user based on the access token
		 
		Map<String, Object> response = new HashMap<String, Object>();
		 
		switch (entityType.toLowerCase()) {
        case "user":
            User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
            response.put("success", true);
    		response.put("message", "user retrived successfully");
    		response.put("entity", user);
    		return ResponseEntity.ok(response);
        
        case "ngo":
            NGO ngo = ngoRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
            response.put("success", true);
    		response.put("message", "ngo retrived successfully");
    		response.put("entity", ngo);
    		return ResponseEntity.ok(response);
             
        case "veterinarian":
            Veterinarian veterinarian = veterinarianRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
            response.put("success", true);
    		response.put("message", "veterinarian retrived successfully");
    		response.put("entity", veterinarian);
    		return ResponseEntity.ok(response);
             
         
    }
		 
		 
 
		response.put("success", false);
		response.put("message", "Entity with specified id not available");
		 
		return ResponseEntity.ok(response);
	}
	

}
