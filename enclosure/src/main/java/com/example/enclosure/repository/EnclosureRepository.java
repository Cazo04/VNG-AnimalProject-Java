package com.example.enclosure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.enclosure.entity.Enclosure;

@Repository
public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {
}
