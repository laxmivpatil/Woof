package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.techverse.Model.PushNotification;
 
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

}
