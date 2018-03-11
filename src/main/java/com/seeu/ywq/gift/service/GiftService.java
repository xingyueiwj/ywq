package com.seeu.ywq.gift.service;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.gift.model.Gift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GiftService {
    Gift save(Gift gift);

    Gift findOne(Long id) throws ResourceNotFoundException;

    Page<Gift> findAll(Pageable pageable);

    Gift update(Gift gift) throws ResourceNotFoundException;

    void delete(Long id);
}
