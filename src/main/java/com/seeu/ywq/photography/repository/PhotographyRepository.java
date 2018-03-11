package com.seeu.ywq.photography.repository;

import com.seeu.ywq.photography.model.Photography;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PhotographyRepository extends JpaRepository<Photography, Long> {
    Page findAllByUidAndDeleteFlag(@Param("uid") Long uid, @Param("deleteFlag") Photography.DELETE_FLAG deleteFlag, Pageable pageable);
}
