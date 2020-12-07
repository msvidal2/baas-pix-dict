package com.picpay.banking.claim.repository;/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, String> {

    @Query("SELECT c FROM claim c WHERE c.key = :key AND c.status in :openStatus")
    ClaimEntity findOpenClaimByKey(String key, List<ClaimSituation> openStatus);

    @Query("SELECT c FROM claim c WHERE c.id = :id AND c.claimerParticipant = :ispb")
    ClaimEntity findClaimerClaimById(String id, Integer ispb);

    @Query("SELECT c FROM claim c WHERE c.id = :id AND c.donorParticipant = :ispb")
    ClaimEntity findDonorClaimById(String id, Integer ispb);

    @Query("SELECT c FROM claim c WHERE c.claimerParticipant = :claimerParticipant AND c.lastModified BETWEEN :startDate AND :endDate")
    Page<ClaimEntity> findAllClaimsWhereIsClaimer(Integer claimerParticipant, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT c FROM claim c WHERE c.donorParticipant = :donorParticipant AND c.lastModified BETWEEN :startDate AND :endDate")
    Page<ClaimEntity> findAllClaimsWhereIsDonor(Integer donorParticipant, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT c FROM claim c WHERE (c.claimerParticipant = :ispb OR c.donorParticipant = :ispb) AND c.status in :openStatus")
    Page<ClaimEntity> findAllPendingClaims(int ispb, List<ClaimSituation> openStatus, Pageable pageable);

}
