package com.example.techverse.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.Model.NGO;
import com.example.techverse.Model.ShippingAddress;
import com.example.techverse.Model.User;
import com.example.techverse.Model.Veterinarian;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.ShippingAddressRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;
import com.example.techverse.exception.UnauthorizedAccessException;
import com.example.techverse.service.UserService;
 
 

 
@RestController
@RequestMapping("/api/address")
public class AddressController {
	
	
	
	@Autowired
	private ShippingAddressRepository shippingAddressRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private VeterinarianRepository veterinarianRepository;

	@Autowired
	private NGORepository NgoRepository;

	@PostMapping("/addshippingaddress/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> addShippingAddress(@PathVariable String entityType, @PathVariable Long entityId,@RequestBody ShippingAddress shippingAddress,@RequestHeader("Authorization") String jwt)throws UnauthorizedAccessException{
        // Retrieve the user by ID (you may adjust this based on your authentication mechanism)
		 Map<String,Object> response = new HashMap<>();
    	switch (entityType.toLowerCase()) {
    case "user":
		User user = userRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("User not found"));
		  shippingAddress.setUser(user);
	        shippingAddressRepository.save(shippingAddress);
	        // Add the shipping address to the user's list of shipping addresses
	        user.getShippingAddresses().forEach(address -> address.setSetDefaultAddress(false));

	        user.getShippingAddresses().add(shippingAddress);
	        userRepository.save(user);
	        
	       
	        response.put("ShippingAddress", shippingAddress);

	        response.put("status", true);
	        response.put("message", "shipping Address added successfully");
	        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
		 
	case "ngo":
		NGO ngo = NgoRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
		shippingAddress.setNgo(ngo);
        shippingAddressRepository.save(shippingAddress);
        // Add the shipping address to the user's list of shipping addresses
        ngo.getShippingAddresses().forEach(address -> address.setSetDefaultAddress(false));

      ngo.getShippingAddresses().add(shippingAddress);
        NgoRepository.save(ngo);
         
        response.put("ShippingAddress", shippingAddress);

        response.put("status", true);
        response.put("message", "shipping Address added successfully");
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
	 

	case "veterinarian":
		Veterinarian veterinarian = veterinarianRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
		shippingAddress.setVeterinarian(veterinarian);
				shippingAddressRepository.save(shippingAddress);
        // Add the shipping address to the user's list of shipping addresses
				veterinarian.getShippingAddresses().forEach(address -> address.setSetDefaultAddress(false));

				veterinarian.getShippingAddresses().add(shippingAddress);
				veterinarianRepository.save(veterinarian);
         
        response.put("ShippingAddress", shippingAddress);

        response.put("status", true);
        response.put("message", "shipping Address added successfully");
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
		 
    	}
    	// Set the user for the shipping address
    	return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }
    @GetMapping("/setshippingaddress/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> setShippingAddress(@PathVariable String entityType, @PathVariable Long entityId,@RequestParam Long addressId,@RequestHeader("Authorization") String jwt)throws UnauthorizedAccessException  {
        // Retrieve the user by ID (you may adjust this based on your authentication mechanism)
    	
    	 Map<String,Object> response = new HashMap<>();
    	 Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
         if (optionalShippingAddress.isEmpty()) {
             // Handle case where address ID is not found
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("status", false);
             errorResponse.put("message", "Shipping address not found for ID: " + addressId);
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
         }
         ShippingAddress shippingAddress = optionalShippingAddress.get();
         shippingAddress.setSetDefaultAddress(true);
     	switch (entityType.toLowerCase()) {
     case "user":
 		User user = userRepository.findById(entityId)
 				.orElseThrow(() -> new UnauthorizedAccessException("User not found"));
 		 user.getShippingAddresses().forEach(address -> {
            if (!address.getId().equals(addressId)) {
                address.setSetDefaultAddress(false);
            }
        });

        // Save the updated shipping address and user
        shippingAddressRepository.save(shippingAddress);
        userRepository.save(user);

       
        response.put("ShippingAddress", shippingAddress);
        response.put("status", true);
        response.put("message", "Default shipping address set successfully");

        return ResponseEntity.ok(response);
        
 		 
 	case "ngo":
 		NGO ngo = NgoRepository.findById(entityId)
 				.orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
 		ngo.getShippingAddresses().forEach(address -> {
             if (!address.getId().equals(addressId)) {
                 address.setSetDefaultAddress(false);
             }
         });

         // Save the updated shipping address and user
         shippingAddressRepository.save(shippingAddress);
        NgoRepository.save(ngo);

        
         response.put("ShippingAddress", shippingAddress);
         response.put("status", true);
         response.put("message", "Default shipping address set successfully");

         return ResponseEntity.ok(response);
 	 

 	case "veterinarian":
 		Veterinarian veterinarian = veterinarianRepository.findById(entityId)
 				.orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
 		veterinarian.getShippingAddresses().forEach(address -> {
            if (!address.getId().equals(addressId)) {
                address.setSetDefaultAddress(false);
            }
        });

        // Save the updated shipping address and user
        shippingAddressRepository.save(shippingAddress);
        veterinarianRepository.save(veterinarian);

       
        response.put("ShippingAddress", shippingAddress);
        response.put("status", true);
        response.put("message", "Default shipping address set successfully");

        return ResponseEntity.ok(response);
 		 
     	}
     	// Set the user for the shipping address
     	return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
     
         

 
       

        

        // Set all other addresses of the user to non-default
       
    }
    @GetMapping("/getshippingaddresses/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> getShippingAddressesByUser(@PathVariable String entityType, @PathVariable Long entityId,@RequestHeader("Authorization") String jwt)throws UnauthorizedAccessException {
    	
    	Map<String,Object> response = new HashMap<>();
   	 
    	switch (entityType.toLowerCase()) {
    case "user":
		User user = userRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("User not found"));
		List<ShippingAddress> shippingAddresses = user.getShippingAddresses();
        response.put("status", true);
        response.put("message", "Shipping addresses retrieved successfully");
        response.put("shippingAddresses", shippingAddresses);

        return ResponseEntity.ok(response);
       
		 
	case "ngo":
		NGO ngo = NgoRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
		List<ShippingAddress> shippingAddressesngo = ngo.getShippingAddresses();
        response.put("status", true);
        response.put("message", "Shipping addresses retrieved successfully");
        response.put("shippingAddresses", shippingAddressesngo);

        return ResponseEntity.ok(response);
	 

	case "veterinarian":
		Veterinarian veterinarian = veterinarianRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
		List<ShippingAddress> shippingAddressesvet =veterinarian.getShippingAddresses();
        response.put("status", true);
        response.put("message", "Shipping addresses retrieved successfully");
        response.put("shippingAddresses", shippingAddressesvet);

        return ResponseEntity.ok(response);
		 
    	}
    	// Set the user for the shipping address
    	return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
   	
   	
   	
   	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	 
        
 
        
    }
    @GetMapping("/getshippingaddress/default/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> getDefaultShippingAddressesByUser(@PathVariable String entityType, @PathVariable Long entityId,@RequestHeader("Authorization") String jwt) throws UnauthorizedAccessException  {
        
    	Map<String,Object> response = new HashMap<>();
      	 
    	switch (entityType.toLowerCase()) {
    case "user":
		User user = userRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("User not found"));
		List<ShippingAddress> shippingAddresses = user.getShippingAddresses().stream()
                .filter(ShippingAddress::isSetDefaultAddress)
                .collect(Collectors.toList());

        if (shippingAddresses.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Default shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

       
        response.put("status", true);
        response.put("message", "Default shipping address retrieved successfully");
        response.put("shippingAddresses", shippingAddresses);

        return ResponseEntity.ok(response);
       
		 
	case "ngo":
		NGO ngo = NgoRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("NGO not found"));
		List<ShippingAddress> shippingAddressesngo = ngo.getShippingAddresses().stream()
                .filter(ShippingAddress::isSetDefaultAddress)
                .collect(Collectors.toList());

        if (shippingAddressesngo.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Default shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

       
        response.put("status", true);
        response.put("message", "Default shipping address retrieved successfully");
        response.put("shippingAddresses", shippingAddressesngo);

        return ResponseEntity.ok(response);

	case "veterinarian":
		Veterinarian veterinarian = veterinarianRepository.findById(entityId)
				.orElseThrow(() -> new UnauthorizedAccessException("Veterinarian not found"));
		List<ShippingAddress> shippingAddressesvet = veterinarian.getShippingAddresses().stream()
                .filter(ShippingAddress::isSetDefaultAddress)
                .collect(Collectors.toList());

        if (shippingAddressesvet.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Default shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

       
        response.put("status", true);
        response.put("message", "Default shipping address retrieved successfully");
        response.put("shippingAddresses", shippingAddressesvet);

        return ResponseEntity.ok(response);
		 
    	}
    	// Set the user for the shipping address
    	return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
   	
   	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	

        // Filter shipping addresses where setDefaultAddress is true
        
    }
    
    
    /*
    
    @PutMapping("/editshippingaddress/{addressId}")
    public ResponseEntity<Map<String, Object>> editShippingAddress(
            @PathVariable("addressId") Long addressId,
            @RequestBody ShippingAddress updatedAddress,
            @RequestHeader("Authorization") String jwt)throws UserException {
    	User user =userService.findUserProfileByJwt(jwt).get();
        

        
        Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
        if (optionalShippingAddress.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ShippingAddress existingAddress = optionalShippingAddress.get();
        // Check if the existing address belongs to the user
        if (!existingAddress.getUser().equals(user)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address does not belong to the user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // Update the existing address with the new data
        existingAddress.setStreet1(updatedAddress.getStreet1());
        existingAddress.setStreet2(updatedAddress.getStreet2());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setPincode(updatedAddress.getPincode());
        existingAddress.setMobile(updatedAddress.getMobile());
        existingAddress.setAlternateMobile(updatedAddress.getAlternateMobile());
        existingAddress.setLandmark(updatedAddress.getLandmark());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setCountry(updatedAddress.getCountry());
        existingAddress.setAddressType(updatedAddress.getAddressType());
        existingAddress.setSetDefaultAddress(updatedAddress.isSetDefaultAddress());

        shippingAddressRepository.save(existingAddress);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Shipping address updated successfully");
        response.put("shippingAddress", existingAddress);

        return ResponseEntity.ok(response);
    }
    
    
    @DeleteMapping("/deleteshippingaddress/{addressId}")
    public ResponseEntity<Map<String, Object>> deleteShippingAddress(
            @PathVariable("addressId") Long addressId,
            @RequestHeader("Authorization") String jwt) throws UserException {
    	User user =userService.findUserProfileByJwt(jwt).get();
        

        
        Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
        if (optionalShippingAddress.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ShippingAddress shippingAddress = optionalShippingAddress.get();
        // Check if the shipping address belongs to the user
        if (!shippingAddress.getUser().equals(user)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address does not belong to the user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        shippingAddressRepository.delete(shippingAddress);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Shipping address deleted successfully");

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getshippingaddress/{addressId}")
    public ResponseEntity<Map<String, Object>> getShippingAddressById(
            @PathVariable("addressId") Long addressId,
            @RequestHeader("Authorization") String jwt) {
        Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
        if (optionalShippingAddress.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ShippingAddress shippingAddress = optionalShippingAddress.get();

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Shipping address retrieved successfully");
        response.put("shippingAddress", shippingAddress);

        return ResponseEntity.ok(response);
    }
    */
}
