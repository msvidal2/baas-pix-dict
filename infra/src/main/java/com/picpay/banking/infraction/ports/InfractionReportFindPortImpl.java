/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.infraction.ports.picpay.InfractionReportRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@RequiredArgsConstructor
public class InfractionReportFindPortImpl implements InfractionReportFindPort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public InfractionReport find(final String infractionReportId) {
        return infractionReportRepository.findById(infractionReportId)
            .map(InfractionReportEntity::toDomain)
            .orElseThrow(() -> new InfractionReportException(InfractionReportError.INFRACTION_REPORT_NOT_FOUND));
    }

    @Override
    public Optional<InfractionReport> findByEndToEndId(final String endToEndId) {
        return null; //infractionReportRepository.findByEndToEndId(endToEndId).map(InfractionReportEntity::toDomain);
    }

}
