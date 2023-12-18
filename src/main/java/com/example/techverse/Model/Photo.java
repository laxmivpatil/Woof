package com.example.techverse.Model;
 
import javax.persistence.*;
import javax.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "rescue_request_id")
    private AnimalRescueRequest rescueRequest;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public AnimalRescueRequest getRescueRequest() {
		return rescueRequest;
	}

	public void setRescueRequest(AnimalRescueRequest rescueRequest) {
		this.rescueRequest = rescueRequest;
	}

	public Photo(Long id, String filename, byte[] data, AnimalRescueRequest rescueRequest) {
		super();
		this.id = id;
		this.filename = filename;
		this.data = data;
		this.rescueRequest = rescueRequest;
	}

	public Photo() {
		super();
		// TODO Auto-generated constructor stub
	}

    // Constructors, getters, and setters
    
    
    
    
}


