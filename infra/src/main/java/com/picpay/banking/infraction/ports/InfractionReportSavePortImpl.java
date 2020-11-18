/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@RequiredArgsConstructor
public class InfractionReportSavePortImpl implements InfractionReportSavePort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public void save(final InfractionReport infractionReport) {
        infractionReportRepository.save(InfractionReportEntity.fromDomain(infractionReport));
    }

}
