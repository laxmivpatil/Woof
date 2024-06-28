package com.example.techverse.Model;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ngo_id", nullable = true)
    private NGO ngo;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = true)
    private Veterinarian veterinarian;
    private LocalDateTime timestamp;
    
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "rescue_request_id", nullable = true)
    private AnimalRescueRequest rescueRequest;
    
    private String rescuepostby;

    @Column(name = "is_read")
    private boolean isRead;

    // Constructors, getters, setters, and other methods...

    public Notification() {
    }

    public Notification(User user, String message, LocalDateTime timestamp, boolean isRead,AnimalRescueRequest rescueRequest) {
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.rescueRequest=rescueRequest;
        
    }

    public Notification(NGO ngo, String message, LocalDateTime timestamp, boolean isRead,AnimalRescueRequest rescueRequest) {
        this.ngo = ngo;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
         
        this.rescueRequest=rescueRequest;
    }

    public Notification(Veterinarian veterinarian, String message, LocalDateTime timestamp, boolean isRead,AnimalRescueRequest rescueRequest) {
        this.veterinarian = veterinarian;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
         
        this.rescueRequest=rescueRequest;
    }
    public Notification(NotificationType type, String message, User user) {
        this.type = type;
        this.message = message;
        this.user = user;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
 
    }

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public String getRescuepostby() {
		return rescuepostby;
	}

	public void setRescuepostby(String rescuepostby) {
		this.rescuepostby = rescuepostby;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    // Override toString() method...

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", read=" + isRead +
                '}';
    }
    
    
    @Column(name = "notifications_enabled" ,columnDefinition = "BOOLEAN") // New field for enabling/disabling notifications
    private Boolean notificationsEnabled;

    // ... (other getters and setters)

    public Boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    

    public Boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
	}

	public Veterinarian getVeterinarian() {
		return veterinarian;
	}

	public void setVeterinarian(Veterinarian veterinarian) {
		this.veterinarian = veterinarian;
	}

	public AnimalRescueRequest getRescueRequest() {
		return rescueRequest;
	}

	public void setRescueRequest(AnimalRescueRequest rescueRequest) {
		this.rescueRequest = rescueRequest;
	}
	
	
	
}
