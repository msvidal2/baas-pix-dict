/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.claim.repository;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimMetricRepository extends JpaRepository<ClaimEntity, String> {

    Long countByStatus(ClaimSituation situation);

}
