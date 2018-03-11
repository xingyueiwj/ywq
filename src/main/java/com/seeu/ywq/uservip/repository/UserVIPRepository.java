package com.seeu.ywq.uservip.repository;

import com.seeu.ywq.uservip.model.UserVIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserVIPRepository extends JpaRepository<UserVIP, Long> {
}
