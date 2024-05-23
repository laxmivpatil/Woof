package com.example.techverse.DTO;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.techverse.Model.Notification;
import com.example.techverse.Model.NotificationType;
import com.example.techverse.Model.User;
import com.example.techverse.service.TimeDifferenceUtil;

public class NotificationDTO {
	 

	     private Long id;

	     private NotificationType type;

	    private String message;

	    
	    private LocalDateTime timestamp;
	    private String timediff;

	     private boolean isRead;
	     
	     private Long rescueId;
	     private String profile;
	     
	     

	    // Constructors, getters, setters, and other methods...

	     
	    public NotificationDTO(Notification notification) {
	    	this.id=notification.getId();
	        this.type = notification.getType();
	        this.message = notification.getMessage();
	         
	        this.timestamp = notification.getTimestamp();
	        this.isRead = notification.isRead();
	       this.rescueId=notification.getRescueRequest().getId();
	       if(notification.getRescuepostby().equals("user")) {
	        this.profile=notification.getRescueRequest().getUser().getProfile();
	       }
	       else if(notification.getRescuepostby().equals("ngo")) {
		        this.profile=notification.getRescueRequest().getNgo().getNGOProfile();
		       }
	       else if(notification.getRescuepostby().equals("veterinarian")) {
		        this.profile=notification.getRescueRequest().getVeterinarian().getVeterinarianProfile();
		       }
	       
	       this.timediff=TimeDifferenceUtil.formatTimeDifference(notification.getRescueRequest().getDatetime(),timestamp);
	       
	    }

	    // Getters and setters...

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public NotificationType getType() {
	        return type;
	    }

	    public void setType(NotificationType type) {
	        this.type = type;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    
	    public LocalDateTime getTimestamp() {
	        return timestamp;
	    }

	    public void setTimestamp(LocalDateTime timestamp) {
	        this.timestamp = timestamp;
	    }

	    public boolean isRead() {
	        return isRead;
	    }

	    public void setRead(boolean read) {
	        this.isRead = read;
	    }

		public Long getRescueId() {
			return rescueId;
		}

		public void setRescueId(Long rescueId) {
			this.rescueId = rescueId;
		}

		public String getTimediff() {
			return timediff;
		}

		public void setTimediff(String timediff) {
			this.timediff = timediff;
		}

		public String getProfile() {
			return profile;
		}

		public void setProfile(String profile) {
			this.profile = profile;
		}

	    // Override toString() method...

	     
	    
	    
	   
}
