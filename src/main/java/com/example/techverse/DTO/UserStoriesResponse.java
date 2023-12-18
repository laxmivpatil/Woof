package com.example.techverse.DTO;

import java.util.List;

public class UserStoriesResponse {
    private List<StoryResponse> publicStories;
    private List<StoryResponse> followersStories;

    public UserStoriesResponse(List<StoryResponse> publicStories, List<StoryResponse> followersStories) {
        this.publicStories = publicStories;
        this.followersStories = followersStories;
    }

	public List<StoryResponse> getPublicStories() {
		return publicStories;
	}

	public void setPublicStories(List<StoryResponse> publicStories) {
		this.publicStories = publicStories;
	}

	public List<StoryResponse> getFollowersStories() {
		return followersStories;
	}

	public void setFollowersStories(List<StoryResponse> followersStories) {
		this.followersStories = followersStories;
	}

	@Override
	public String toString() {
		return "UserStoriesResponse [publicStories=" + publicStories + ", followersStories=" + followersStories + "]";
	}

    
    // Getters and setters for publicStories and followersStories
}

