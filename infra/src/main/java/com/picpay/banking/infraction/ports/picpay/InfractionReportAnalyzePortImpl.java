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


@Component
@RequiredArgsConstructor
public class InfractionReportAnalyzePortImpl implements InfractionReportAnalyzePort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public InfractionReport analyze(InfractionReport infractionReportAnalysed) {

        final Optional<InfractionReportEntity> infractionOptional = infractionReportRepository.findById(infractionReportAnalysed.getInfractionReportId().toUpperCase());
        var result =  infractionOptional.map(inf ->  {
            inf.setAnalyzeResult(infractionReportAnalysed.getAnalyze().getAnalyzeResult());
            inf.setAnalyzeDetails(infractionReportAnalysed.getAnalyze().getDetails());
            inf.setLastUpdatedDate(infractionReportAnalysed.getDateLastUpdate());
            infractionReportRepository.saveAndFlush(inf);
            return inf.toDomain();
        }).orElseThrow(() -> new InfractionReportException(InfractionReportError.REPORTED_TRANSACTION_NOT_FOUND));

        return result;
    }
}
