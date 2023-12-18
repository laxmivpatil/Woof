package com.example.techverse.DTO;

public class StoryResponse {
    private Long story_id;
    private String content;
    private String timestamp;
    private int likes;
    private int comments;

    // Constructors, getters, and setters

    public StoryResponse() {
    }

    public StoryResponse(Long story_id, String content, String timestamp,int likes,int comments) {
        this.story_id = story_id;
        this.content = content;
        this.timestamp = timestamp;
        this.likes=likes;
        this.comments=comments;
    }
    

    public Long getStory_id() {
        return story_id;
    }

    public void setStory_id(Long story_id) {
        this.story_id = story_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}
    
    
    
}


