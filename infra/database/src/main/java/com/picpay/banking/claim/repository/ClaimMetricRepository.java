/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.claim.repository;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ClaimMetricRepository extends JpaRepository<ClaimEntity, String> {

    Long countByStatus(ClaimSituation situation);

    @Query("SELECT count(*) FROM claim c WHERE c.type = 'POSSESSION_CLAIM' AND c.status = 'AWAITING_CLAIM' AND c.donorParticipant = :donorParticipant AND c.resolutionPeriodEnd <= :resolutionPeriodEnd")
    Long findAwaitingPossessionClaimsForDonor(Integer donorParticipant, LocalDateTime resolutionPeriodEnd);

    @Query("SELECT count(*) FROM claim c WHERE c.type = 'PORTABILITY' AND c.status = 'AWAITING_CLAIM' AND c.donorParticipant = :donorParticipant AND c.resolutionPeriodEnd <= :resolutionPeriodEnd")
    Long findAwaitingPortabilityClaimsForDonor(Integer donorParticipant, LocalDateTime resolutionPeriodEnd);

    @Query(value = "SELECT count(*) FROM claim c WHERE c.type = 'POSSESSION_CLAIM' AND c.status = 'CONFIRMED' AND c.claimer_participant = :claimerParticipant " +
            "AND datediff(now() , c.resolution_period_end) > :interval", nativeQuery = true)
    Long findAwaitingClaimToCancelForClaimer(Integer claimerParticipant, Integer interval);

}
