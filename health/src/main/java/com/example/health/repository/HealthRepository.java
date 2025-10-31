package com.example.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.health.entity.Health;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {
}
