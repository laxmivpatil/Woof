package com.example.techverse.Model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ngo_id")
    private NGO ngo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private Veterinarian veterinarian;

    @Column(nullable = false)
    private String caption;
    
    @Column(nullable = false)
    private String visibility;

    @Column(nullable = false)
    private String mediaUrl;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    // Constructors, getters, setters, and other methods...

    

    // Getter and setter for updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

   

	public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
      
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Story() {
        this.createdAt = LocalDateTime.now();
		 this.updatedAt = LocalDateTime.now();

    }

     
      

	 




	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
        return id;
    }

    

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @ManyToMany(mappedBy = "storiesFollowed")
    private List<User> followers;

    // Constructors, getters, setters, and other methods...

    // Add a follower to the story
    public void addFollower(User follower) {
        followers.add(follower);
        follower.getStoriesFollowed().add(this);
    }

    // Remove a follower from the story
    public void removeFollower(User follower) {
        followers.remove(follower);
        follower.getStoriesFollowed().remove(this);
    }
    
    public void removeAllFollowers() {
        for (User follower : followers) {
            follower.getStoriesFollowed().remove(this);
        }
        followers.clear();
    }
    public List<User> getFollowers() {
        return followers;
    }

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}



	// Add a field to store the number of likes
	@Column(nullable = false)
	private int likes;

	// Add a many-to-many relationship with users who liked the story
	@ManyToMany
	@JoinTable(
	    name = "story_liked_by_users",
	    joinColumns = @JoinColumn(name = "story_id"),
	    inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> likedBy;

	// Getter and setter methods for likes
	public int getLikes() {
	    return likes;
	}

	public void setLikes(int likes) {
	    this.likes = likes;
	}

	// Add a method to like the story
	public void likeStory(User user) {
	    if (!likedBy.contains(user)) {
	        likedBy.add(user);
	        likes++;
	    }
	}

	// Add a method to unlike the story
	public void unlikeStory(User user) {
	    if (likedBy.contains(user)) {
	        likedBy.remove(user);
	        likes--;
	    }
	}

	
	 
		
	@OneToMany(mappedBy = "story") // Use "story" as the mappedBy attribute
	private List<Comment> commentsBy;

	// Add a method to add a comment to the story
	public void addComment(Comment comment) {
	    commentsBy.add(comment);
	    
	}

	// Add a method to remove a comment from the story
	public void removeComment(Comment comment) {
	    commentsBy.remove(comment);
	}




	public List<User> getLikedBy() {
		return likedBy;
	}




	public void setLikedBy(List<User> likedBy) {
		this.likedBy = likedBy;
	}




	 




	public List<Comment> getCommentsBy() {
		return commentsBy;
	}




	public void setCommentsBy(List<Comment> commentsBy) {
		this.commentsBy = commentsBy;
	}

	@Column(nullable = false)
    private int commentCount; // New field to store the comment count

    // Constructors, getters, setters, and other methods...

    // Add a method to increment the comment count
    public void incrementCommentCount() {
        commentCount++;
    }

    // Add a method to decrement the comment count
    public void decrementCommentCount() {
        if (commentCount > 0) {
            commentCount--;
        }
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    
}