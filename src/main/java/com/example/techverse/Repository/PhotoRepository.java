package com.example.techverse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.techverse.Model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // Add custom query methods if needed
}
 




