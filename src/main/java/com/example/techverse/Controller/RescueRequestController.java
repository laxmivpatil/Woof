package com.example.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.DistanceCalculator;
import com.example.techverse.DTO.AnimalRescueRequestDTO;
import com.example.techverse.DTO.RegistrationDTO;
import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Model.NGO;
import com.example.techverse.Model.Notification;
import com.example.techverse.Model.Photo;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.AnimalRescueRequestRepository;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.NotificationRepository;
import com.example.techverse.Repository.PhotoRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.service.AnimalRescueRequestService;
import com.example.techverse.service.NotificationService;
import com.example.techverse.service.StorageService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animal_rescue")
public class RescueRequestController {

	private final UserRepository userRepository;
	private final AnimalRescueRequestRepository rescueRequestRepository;
	private final PhotoRepository photoRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
    private AnimalRescueRequestService  animalRescueRequestService;
	
	
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private VeterinarianRepository veterinarianRepository;

	@Autowired
	private NGORepository NgoRepository;

	@Autowired
	private StorageService storageService;

	@Autowired
	public RescueRequestController(UserRepository userRepository, AnimalRescueRequestRepository rescueRequestRepository,
			PhotoRepository photoRepository) {
		this.userRepository = userRepository;
		this.rescueRequestRepository = rescueRequestRepository;
		this.photoRepository = photoRepository;
	}
	 

	@PostMapping("/post_rescue_request/{entityType}/{entityId}")
	public ResponseEntity<Map<String, Object>> postRescueRequest(@RequestHeader("Authorization") String authorization,
			@PathVariable String entityType, @PathVariable Long entityId, @RequestPart("role") String role,
			@RequestPart("latitude") String latitude, @RequestPart("longitude") String longitude,
			@RequestPart("location") String location, @RequestPart("priority_issue") String priorityIssue,
			@RequestPart("contact_details") String contactDetails, @RequestPart("caption") String caption,
			@RequestPart(value = "imgorvideo", required = false) MultipartFile imgorvideo)
			throws IOException, UnauthorizedAccessException {
		 
		double lan = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longitude);
		AnimalRescueRequest rescueRequest = new AnimalRescueRequest();
		LocalDateTime dateTime = LocalDateTime.now();

        // Set the time zone to GMT+5:30 (India Standard Time)
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalDateTime dateTimeInIndia = dateTime.atZone(zoneId).toLocalDateTime();
        String message = "A new rescue request has been created at " + location ;

		switch (entityType.toLowerCase()) {
		case "user":
			User user = userRepository.findById(entityId)
					.orElseThrow(() -> new UnauthorizedAccessException("User not found"));

			rescueRequest.setUser(user);
			rescueRequest.setVeterinarian(null);
			rescueRequest.setNgo(null);
			rescueRequest.setLatitude(lan);
			rescueRequest.setLongitude(lon);
			rescueRequest.setLocation(location);
			rescueRequest.setPriorityIssue(priorityIssue);
			rescueRequest.setContactDetails(contactDetails);
			rescueRequest.setCaption(caption);
			rescueRequest.setDatetime(dateTimeInIndia);
			// Save the rescue request in the database

			// Process photos if available
			if (imgorvideo != null && !imgorvideo.isEmpty()) {
				String path = storageService.uploadFileOnAzure(imgorvideo);
				rescueRequest.setImgorvideo(path);
			}

			rescueRequest = rescueRequestRepository.save(rescueRequest);
			break;
		case "ngo":
			NGO ngo = NgoRepository.findById(entityId)
					.orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));

			rescueRequest.setUser(null);
			rescueRequest.setVeterinarian(null);
			rescueRequest.setNgo(ngo);
			rescueRequest.setLatitude(lan);
			rescueRequest.setLongitude(lon);
			rescueRequest.setLocation(location);
			rescueRequest.setPriorityIssue(priorityIssue);
			rescueRequest.setContactDetails(contactDetails);
			rescueRequest.setCaption(caption);
			rescueRequest.setDatetime(dateTimeInIndia);
			// Save the rescue request in the database

			// Process photos if available
			if (imgorvideo != null && !imgorvideo.isEmpty()) {
				String path = storageService.uploadFileOnAzure(imgorvideo);
				rescueRequest.setImgorvideo(path);
			}

			rescueRequest = rescueRequestRepository.save(rescueRequest);
			break;

		case "veterinarian":
			Veterinarian veterinarian = veterinarianRepository.findById(entityId)
					.orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
			rescueRequest.setUser(null);
			rescueRequest.setVeterinarian(veterinarian);
			rescueRequest.setNgo(null);
			rescueRequest.setLatitude(lan);
			rescueRequest.setLongitude(lon);
			rescueRequest.setLocation(location);
			rescueRequest.setPriorityIssue(priorityIssue);
			rescueRequest.setContactDetails(contactDetails);
			rescueRequest.setCaption(caption);
			rescueRequest.setDatetime(dateTimeInIndia);
			System.out.println(dateTimeInIndia);

			// Save the rescue request in the database

			// Process photos if available
			if (imgorvideo != null && !imgorvideo.isEmpty()) {
				String path = storageService.uploadFileOnAzure(imgorvideo);
				rescueRequest.setImgorvideo(path);
			}

			rescueRequest = rescueRequestRepository.save(rescueRequest);
			break;

		}

		Map<String, Object> response = new HashMap<String, Object>();
		double roundedLatitude = Math.round(lan * 1e6) / 1e6;
		double roundedLongitude = Math.round(lon * 1e6) / 1e6;
		List<NGO> nearbyNGO = NgoRepository.findNearbyNGO(roundedLatitude, roundedLongitude, 5.0);
		List<Veterinarian> nearbyVeterinarian = veterinarianRepository.findNearbyVeterinarian(roundedLatitude,
				roundedLongitude, 5.0);
		List<RegistrationDTO> registrationDTOs = new ArrayList<>();
		for (NGO nearbyNgo : nearbyNGO) {
	        if (!entityType.equalsIgnoreCase("ngo")) { // Avoid sending notification to the same NGO
	            RegistrationDTO dto = new RegistrationDTO();
	            registrationDTOs.add(dto.toDTO(nearbyNgo));
	            Notification n=notificationService.sendNotificationToNGO(nearbyNgo, message,rescueRequest);
	            n.setRescuepostby(entityType);
	            notificationRepository.save(n);
	        }
	        else if(!nearbyNgo.getId().equals(entityId)) {
	        	RegistrationDTO dto = new RegistrationDTO();
	            registrationDTOs.add(dto.toDTO(nearbyNgo));
	            Notification n=notificationService.sendNotificationToNGO(nearbyNgo, message,rescueRequest);
	            n.setRescuepostby(entityType);
	            notificationRepository.save(n);
	        }
	    }

	    for (Veterinarian nearbyVet : nearbyVeterinarian) {
	        if ( !entityType.equalsIgnoreCase("veterinarian")) { // Avoid sending notification to the same Veterinarian
	            RegistrationDTO dto = new RegistrationDTO();
	            registrationDTOs.add(dto.toDTO(nearbyVet));
	            Notification n= notificationService.sendNotificationToVeterinarian(nearbyVet, message,rescueRequest);
	            n.setRescuepostby(entityType);
	            notificationRepository.save(n);
	        }
	        else if(!nearbyVet.getId().equals(entityId))
	        {
	            RegistrationDTO dto = new RegistrationDTO();
	            registrationDTOs.add(dto.toDTO(nearbyVet));
	            Notification n=notificationService.sendNotificationToVeterinarian(nearbyVet, message,rescueRequest);
	            n.setRescuepostby(entityType);
	            notificationRepository.save(n);
	            
	        }
	    }

		response.put("success", true);
		response.put("message", "Rescue request posted successfully");
		response.put("nearbyngoandvet", registrationDTOs);

//  response.setData(token);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user_rescue_requests/{entityType}/{entityId}")
	public ResponseEntity<Map<String,Object>> getUserRescueRequests(
			@RequestHeader("Authorization") String accessToken,@PathVariable String entityType, @PathVariable Long entityId,
			@RequestParam("lat") String latitude,@RequestParam("long") String longitude) throws IOException, UnauthorizedAccessException {
		// Find the user based on the access token
		double lan = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longitude);
		Map<String, Object> response = new HashMap<String, Object>();
		double roundedLatitude = Math.round(lan * 1e6) / 1e6;
		double roundedLongitude = Math.round(lon * 1e6) / 1e6;
		switch (entityType.toLowerCase()) {
        case "user":
            User user = userRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
            break;
        case "ngo":
            NGO ngo = NgoRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
            break;
        case "veterinarian":
            Veterinarian veterinarian = veterinarianRepository.findById(entityId).orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
            break;
         
    }
		 
		 

		// Retrieve rescue requests posted by the user
		List<AnimalRescueRequest> userRescueRequests = rescueRequestRepository.findAllByOrderByDatetimeDesc();
		List<AnimalRescueRequestDTO> dto1=new ArrayList<AnimalRescueRequestDTO>();
		for (AnimalRescueRequest request : userRescueRequests) {
			System.out.println(request.getId()+" "+request.getDatetime());
			AnimalRescueRequestDTO a=animalRescueRequestService.mapToDTO(request);
			double distance=DistanceCalculator.calculateDistance(lan, lon, request.getLatitude(),request.getLongitude());
			a.setDistance(distance);
			
			dto1.add(a);
			
		}
		response.put("success", true);
		response.put("message", "Post retrived successfully");
		response.put("post", dto1);
		return ResponseEntity.ok(response);
	}
	
	
	
	
	
	
	/*
	 @GetMapping("/user_rescue_requests")
	public ResponseEntity<List<AnimalRescueRequest>> getUserRescueRequests(
			@RequestHeader("Authorization") String accessToken,@PathVariable String entityType, @PathVariable Long entityId,
			@RequestParam("lat") String latitude,@RequestParam("long") String longitude
			) {
		// Find the user based on the access token

		Optional<User> userOptional = userRepository.findByToken(accessToken.substring(7));
		if (!userOptional.isPresent()) {

			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		System.out.println("hi how are you");
		User user = userOptional.get();

		// Retrieve rescue requests posted by the user
		List<AnimalRescueRequest> userRescueRequests = rescueRequestRepository.findByUser(user);
		for (AnimalRescueRequest request : userRescueRequests) {
			System.out.println(request.getId());
		}
		return new ResponseEntity<>(userRescueRequests, HttpStatus.OK);
	}
	*/
	@GetMapping("/own_rescue_requests/{entityType}/{entityId}")
	public ResponseEntity<Map<String, Object>> getRescueRequests(
	        @RequestHeader("Authorization") String accessToken, @PathVariable String entityType, @PathVariable Long entityId,
	        @RequestParam("lat") String latitude, @RequestParam("long") String longitude) throws IOException, UnauthorizedAccessException {
	    // Find the user/NGO/veterinarian based on the access token
	    double lan = Double.parseDouble(latitude);
	    double lon = Double.parseDouble(longitude);
	    Map<String, Object> response = new HashMap<>();
	    double roundedLatitude = Math.round(lan * 1e6) / 1e6;
	    double roundedLongitude = Math.round(lon * 1e6) / 1e6;

	    List<AnimalRescueRequestDTO> dtoList = new ArrayList<>();

	    switch (entityType.toLowerCase()) {
	        case "user":
	            User user = userRepository.findById(entityId)
	                    .orElseThrow(() -> new UnauthorizedAccessException("User not found"));

	            // Retrieve rescue requests posted by the user
	            List<AnimalRescueRequest> userRescueRequests = rescueRequestRepository.findByUserOrderByDatetimeDesc(user);
	            for (AnimalRescueRequest request : userRescueRequests) {
	                AnimalRescueRequestDTO a = animalRescueRequestService.mapToDTO(request);
	                double distance = DistanceCalculator.calculateDistance(lan, lon, request.getLatitude(), request.getLongitude());
	                a.setDistance(distance);
	                dtoList.add(a);
	            }
	            break;
	        case "ngo":
	            NGO ngo = NgoRepository.findById(entityId)
	                    .orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));

	            // Retrieve rescue requests associated with the NGO
	            List<AnimalRescueRequest> ngoRescueRequests = rescueRequestRepository.findByNgoOrderByDatetimeDesc(ngo);
	            for (AnimalRescueRequest request : ngoRescueRequests) {
	                AnimalRescueRequestDTO a = animalRescueRequestService.mapToDTO(request);
	                double distance = DistanceCalculator.calculateDistance(lan, lon, request.getLatitude(), request.getLongitude());
	                a.setDistance(distance);
	                dtoList.add(a);
	            }
	            break;
	        case "veterinarian":
	            Veterinarian veterinarian = veterinarianRepository.findById(entityId)
	                    .orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));

	            // Retrieve rescue requests associated with the veterinarian
	            List<AnimalRescueRequest> vetRescueRequests = rescueRequestRepository.findByVeterinarianOrderByDatetimeDesc(veterinarian);
	            for (AnimalRescueRequest request : vetRescueRequests) {
	                AnimalRescueRequestDTO a = animalRescueRequestService.mapToDTO(request);
	                double distance = DistanceCalculator.calculateDistance(lan, lon, request.getLatitude(), request.getLongitude());
	                a.setDistance(distance);
	                dtoList.add(a);
	            }
	            break;
	        default:
	            response.put("success", false);
	            response.put("message", "Invalid entity type");
	            return ResponseEntity.ok(response);
	    }

	    response.put("success", true);
	    response.put("message", "Rescue requests retrieved successfully");
	    response.put("requests", dtoList);
	    return ResponseEntity.ok(response);
	}

	 
}
