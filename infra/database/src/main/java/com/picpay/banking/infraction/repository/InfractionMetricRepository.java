/*
 *  baas-pix-dict 1.0 27/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.repository;


import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfractionMetricRepository extends JpaRepository<InfractionReportEntity, String> {

    Long countBySituation(InfractionReportSituation situation);


}
