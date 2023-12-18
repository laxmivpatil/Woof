package com.example.techverse.Controller;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.apache.tomcat.util.codec.binary.Base64; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.Model.User;
import com.example.techverse.Repository.UserRepository;

@RestController
@RequestMapping("/api/biometrics")
public class BiometricsController {
    @Autowired
    private UserRepository userRepository; // If you're using Spring Data JPA

    @PostMapping("/register-fingerprint")
    public ResponseEntity<Map<String, Object>> registerFingerprint(
            @RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("user_id");
            String fingerprintTemplate = (String) request.get("fingerprint_template");

            // Validate input
            if (userId == null || fingerprintTemplate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("success", false));
            }

            // Check if the user exists (you may need to modify this depending on your data store)
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
            

            if (userOptional.isPresent()) {
            	 // Encode fingerprint template to bytes
                byte[] fingerprintBytes = fingerprintTemplate.getBytes(StandardCharsets.UTF_8);
                System.out.println("djghjdhgj"+fingerprintBytes);
                String encodedFingerprint = Base64.encodeBase64String(fingerprintBytes);
                System.out.println("djghjdhgj"+encodedFingerprint);
                User user = userOptional.get();
                user.setFingerprint_template(encodedFingerprint);
                
                userRepository.save(user);
                return ResponseEntity.ok(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("success", false));
            }
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("success", false));
        }
    } 
    
    
    
    @PostMapping("/authenticate-fingerprint")
    public ResponseEntity<Map<String, Object>> authenticateFingerprint(
            @RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("user_id");
            String providedFingerprintTemplate = (String) request.get("fingerprint_template");

            // Validate input
            if (userId == null || providedFingerprintTemplate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("success", false));
            }

            // Check if the user exists
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Decode the stored fingerprint template
                String storedFingerprintTemplate = user.getFingerprint_template();
                byte[] decodedStoredTemplate = Base64.decodeBase64(storedFingerprintTemplate);

                // Compare the provided fingerprint template with the decoded stored template
                if (providedFingerprintTemplate.equals(new String(decodedStoredTemplate))) {
                    // Fingerprint templates match, authentication successful
                    return ResponseEntity.ok(Collections.singletonMap("success", true));
                } else {
                    // Fingerprint templates do not match, authentication failed
                    return ResponseEntity.ok(Collections.singletonMap("success", false));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("success", false));
            }
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("success", false));
        }
    }
    
    
    @PostMapping("/register-face")
    public ResponseEntity<Map<String, Object>> registerFace(
            @RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("user_id");
            String faceTemplate = (String) request.get("face_template");

            // Validate input
            if (userId == null || faceTemplate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("success", false));
            }

            // Check if the user exists
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Encode the face template using Base64
                byte[] encodedFaceTemplate = Base64.encodeBase64(faceTemplate.getBytes());

                // Store the encoded face template in the user's profile
                user.setFace_template(new String(encodedFaceTemplate));

                userRepository.save(user);
                return ResponseEntity.ok(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("success", false));
            }
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("success", false));
        }
    }

    
    @PostMapping("/authenticate-face")
    public ResponseEntity<Map<String, Object>> authenticateFace(
            @RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("user_id");
            String providedFaceTemplate = (String) request.get("face_template");

            // Validate input
            if (userId == null || providedFaceTemplate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("success", false));
            }

            // Check if the user exists
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Decode the stored face template using Base64
                String storedFaceTemplate = user.getFace_template();
                byte[] decodedStoredTemplate = Base64.decodeBase64(storedFaceTemplate);

                // Compare the provided face template with the decoded stored template
                if (providedFaceTemplate.equals(new String(decodedStoredTemplate))) {
                    // Face templates match, authentication successful
                    return ResponseEntity.ok(Collections.singletonMap("success", true));
                } else {
                    // Face templates do not match, authentication failed
                    return ResponseEntity.ok(Collections.singletonMap("success", false));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("success", false));
            }
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("success", false));
        }
    }
}
