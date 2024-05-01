package com.example.techverse.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.DTO.AnimalRescueRequestDTO;
import com.example.techverse.Model.AnimalRescueRequest;
import com.example.techverse.Repository.AnimalRescueRequestRepository;
import com.example.techverse.Repository.NGORepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.Repository.VeterinarianRepository;

@Service
public class AnimalRescueRequestService {

    private final AnimalRescueRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final NGORepository ngoRepository;
    private final VeterinarianRepository veterinarianRepository;

    @Autowired
    public AnimalRescueRequestService(AnimalRescueRequestRepository requestRepository, UserRepository userRepository,
                                      NGORepository ngoRepository, VeterinarianRepository veterinarianRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.ngoRepository = ngoRepository;
        this.veterinarianRepository = veterinarianRepository;
    }

    public List<AnimalRescueRequestDTO> getAllRescueRequestsOrderedByDatetime() {
        List<AnimalRescueRequest> rescueRequests = requestRepository.findAllByOrderByDatetimeDesc();
        return rescueRequests.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public AnimalRescueRequestDTO mapToDTO(AnimalRescueRequest request) {
        AnimalRescueRequestDTO dto = new AnimalRescueRequestDTO();
        dto.setId(request.getId());
        dto.setDatetimeFormatted(request.getDatetime().format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mma")));
        dto.setLocation(request.getLocation());
        dto.setCaption(request.getCaption());
        dto.setImgorvideo(request.getImgorvideo());
        
        // Fetch user, NGO, and veterinarian details
        if (request.getUser() != null) {
            
                dto.setUserName(request.getUser().getFullName());
                dto.setUserProfile(request.getUser().getProfile());
                dto.setRole("user");
                dto.setUserId(request.getUser().getId());  
                dto.setUserAddress("");
        }
        if (request.getNgo() != null) {
                dto.setUserName(request.getNgo().getFullName());
                dto.setUserProfile(request.getNgo().getNGOProfile());
                dto.setRole("ngo"); 
                dto.setUserId(request.getNgo().getId());  
                dto.setUserAddress(request.getNgo().getAddress());
            
        }
        if (request.getVeterinarian() != null) {
        	 dto.setUserName(request.getVeterinarian().getFullName());
             dto.setUserProfile(request.getVeterinarian().getVeterinarianProfile());
             dto.setRole("veterinarian"); 
             dto.setUserId(request.getVeterinarian().getId());  
             dto.setUserAddress(request.getVeterinarian().getAddress());
        }
        
        return dto;
    }
}