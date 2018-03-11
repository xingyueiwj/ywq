package com.seeu.ywq.trend.repository;

import com.seeu.ywq.trend.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}
