package com.example.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.staff.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
}
