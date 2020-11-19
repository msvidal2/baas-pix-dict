/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

        var infractionReportEntity = infractionReportRepository.findById(infractionReportId).orElseThrow(RuntimeException::new);

        return InfractionReport.builder()
         .infractionReportId(infractionReportEntity.getInfractionReportId())
         .endToEndId(infractionReportEntity.getEndToEndId())
         .reportedBy(infractionReportEntity.getReportedBy())
         .situation(infractionReportEntity.getSituation())
         .ispbDebited(infractionReportEntity.getIspbDebited())
         .ispbCredited(infractionReportEntity.getIspbCredited())
         .dateCreate(infractionReportEntity.getDateCreate())
         .dateLastUpdate(infractionReportEntity.getDateLastUpdate())
         .ispbRequester(infractionReportEntity.getIspbRequester())
         .type(infractionReportEntity.getType())
         .details(infractionReportEntity.getDetails())
         .requestIdentifier(infractionReportEntity.getRequestIdentifier())
         .analyze(InfractionAnalyze.builder().analyzeResult(infractionReportEntity.getAnalyzeResult())
                 .details(infractionReportEntity.getAnalyzeDetails()).build()).build();

    }
}
