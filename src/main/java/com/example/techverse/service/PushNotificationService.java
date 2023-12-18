package com.example.techverse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.Model.DogPost;
import com.example.techverse.Model.PushNotification;
import com.example.techverse.Model.Story;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.PushNotificationRepository;

@Service
public class PushNotificationService {

    @Autowired
    private PushNotificationRepository notificationRepository;

    public void createPushNotification(User follower, User user, String action, DogPost dogPost) {
        PushNotification notification = new PushNotification();
        
        notification.setFollower(follower);
        notification.setUser(user);
        notification.setAction(action);
        notification.setDogPost(dogPost);
        System.out.println(notification);
        notificationRepository.save(notification);
    }
}

