package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.example.techverse.Model.DogPost;

public interface DogPostRepository extends JpaRepositoryImplementation<DogPost, Long> {

}
