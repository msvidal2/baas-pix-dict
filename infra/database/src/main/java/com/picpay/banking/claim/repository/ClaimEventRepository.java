package com.picpay.banking.claim.repository;

import com.picpay.banking.claim.entity.ClaimEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimEventRepository extends JpaRepository<ClaimEventEntity, Long> {
}