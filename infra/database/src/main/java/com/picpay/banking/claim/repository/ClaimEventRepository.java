package com.picpay.banking.claim.repository;

import com.picpay.banking.claim.entity.ClaimEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimEventRepository extends JpaRepository<ClaimEvent, Long> {
}
