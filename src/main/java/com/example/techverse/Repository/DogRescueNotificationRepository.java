package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.DogRescueNotification;

public interface DogRescueNotificationRepository extends JpaRepository<DogRescueNotification, Long> {

}
