package com.example.logging.repository;

import com.example.logging.entity.SysLogging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLoggingRepository extends JpaRepository<SysLogging, Long> {
}
