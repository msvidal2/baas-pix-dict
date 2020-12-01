/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports.picpay;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportAnalyzePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@RequiredArgsConstructor
public class InfractionReportAnalyzePortImpl implements InfractionReportAnalyzePort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public InfractionReport analyze(final String infractionReportId, final Integer ispb, final InfractionAnalyze analyze,
        final LocalDateTime dateLastUpdate, final String requestIdentifier) {

        final Optional<InfractionReportEntity> infractionOptional = infractionReportRepository.findById(infractionReportId.toUpperCase());
        var result =  infractionOptional.map(inf ->  {
            inf.setAnalyzeResult(analyze.getAnalyzeResult());
            inf.setAnalyzeDetails(analyze.getDetails());
            inf.setLastUpdatedDate(dateLastUpdate);
            infractionReportRepository.saveAndFlush(inf);
            return inf.toDomain();
        });

        return result.get();
    }
}
