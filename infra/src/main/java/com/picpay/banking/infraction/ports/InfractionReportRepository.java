/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Repository
public interface InfractionReportRepository extends JpaRepository<InfractionReportEntity, String> {


}
