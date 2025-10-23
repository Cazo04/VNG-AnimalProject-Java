package com.example.report.repository.interfaces;

import com.example.report.entity.Feeding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedingRepository extends JpaRepository<Feeding, Long>, FeedingRepositoryQueryDSL {
}
