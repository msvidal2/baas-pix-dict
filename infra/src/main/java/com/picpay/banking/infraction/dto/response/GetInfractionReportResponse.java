/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.dto.response;

import com.picpay.banking.infraction.dto.request.InfractionReport;
import lombok.Data;


/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Data
public class GetInfractionReportResponse {

    private final InfractionReport infractionReport;

    public com.picpay.banking.pix.core.domain.InfractionReport toDomain() {
        return com.picpay.banking.pix.core.domain.InfractionReport.builder()
            .infractionReportId(infractionReport.getId())
            .type(com.picpay.banking.pix.core.domain.InfractionType.resolve(infractionReport.getInfractionType().getValue()))
            .reportedBy(infractionReport.getReportedBy())
            .transactionId(infractionReport.getTransactionId())
            .details(infractionReport.getReportDetails())
            .ispbDebited(Integer.valueOf(infractionReport.getDebitedParticipant()))
            .ispbCredited(Integer.valueOf(infractionReport.getCreditedParticipant()))
            .dateCreate(infractionReport.getCreationTime())
            .dateLastUpdate(infractionReport.getLastModified())
            .build();
    }
}
