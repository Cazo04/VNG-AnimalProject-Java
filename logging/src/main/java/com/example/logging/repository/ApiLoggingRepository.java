package com.example.logging.repository;

import com.example.logging.entity.ApiLogging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ApiLoggingRepository extends JpaRepository<ApiLogging, UUID> {
}
