package com.seeu.ywq.pay.repository;

import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderLogPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderLogRepository extends JpaRepository<OrderLog, OrderLogPKeys> {
    Page findAllByUid(@Param("uid") Long uid, Pageable pageable);
}
