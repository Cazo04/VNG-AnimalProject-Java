package com.example.feeding.repository;

import com.example.feeding.entity.Feeding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedingRepository extends JpaRepository<Feeding, Long> {
    // Additional query methods can be defined here if needed.
}
