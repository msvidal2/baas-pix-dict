/*
 *  baas-pix-dict 1.0 10/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.reconciliation.entity.SyncVerifierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author rafael.braga
 * @version 1.0 10/02/2021
 */
@Repository
public interface ReconciliationMetricRepository extends JpaRepository<SyncVerifierEntity, String> {

    @Query(value = "select count(1) from sync_verifier s where s.synchronized_at <= (now() - interval 1 day)", nativeQuery = true)
    Long countSyncExecutedLast24Hours();

}
