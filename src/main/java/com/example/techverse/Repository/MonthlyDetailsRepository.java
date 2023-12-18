package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.MonthlyDetails;

//MonthlyDetailsRepository.java
public interface MonthlyDetailsRepository extends JpaRepository<MonthlyDetails, Long> {
}

