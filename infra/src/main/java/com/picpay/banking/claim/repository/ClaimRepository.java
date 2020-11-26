package com.picpay.banking.claim.repository;/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.pix.core.domain.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, String> {

    @Query("SELECT c FROM claim c WHERE c.key = :key AND c.status in :openStatus")
    ClaimEntity findOpenClaimByKey(String key, List<ClaimStatus> openStatus);

    @Query("SELECT c FROM claim c WHERE c.id = :id AND c.claimerParticipant = :ispb")
    ClaimEntity findClaimerClaimById(String id, String ispb);

    @Query("SELECT c FROM claim c WHERE c.id = :id AND c.donorParticipant = :ispb")
    ClaimEntity findDonorClaimById(String id, String ispb);

}
