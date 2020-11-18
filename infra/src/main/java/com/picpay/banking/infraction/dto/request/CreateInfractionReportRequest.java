/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.dto.request;

import lombok.Builder;
import lombok.Getter;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Builder
@Getter
public class CreateInfractionReportRequest {

    private final int participant;
    private final InfractionReport infractionReport;

    public static CreateInfractionReportRequest from(com.picpay.banking.pix.core.domain.InfractionReport infractionReport) {
       return CreateInfractionReportRequest.builder()
           .participant(infractionReport.getIspbRequester())
           .infractionReport(InfractionReport.builder()
                                 .infractionType(InfractionType.from(infractionReport.getType()))
                                 .reportDetails(infractionReport.getDetails())
                                 .transactionId(infractionReport.getEndToEndId())
                                 .build())
           .build();

    }


}
