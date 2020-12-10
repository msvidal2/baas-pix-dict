/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.infraction.repository.InfractionReportRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InfractionReportSavePortImpl implements InfractionReportSavePort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public void save(@NonNull final InfractionReport infractionReport) {
        infractionReportRepository.save(InfractionReportEntity.fromDomain(infractionReport));
        log.info("Infraction_analysis_saved"
            , kv("endToEndId", infractionReport.getEndToEndId())
            , kv("infractionReportId", infractionReport.getInfractionReportId()));
    }

}
