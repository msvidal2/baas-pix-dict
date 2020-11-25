/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.dto.response;

import com.picpay.banking.infraction.dto.request.InfractionReportRequest;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import lombok.Data;


/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Data
public class GetInfractionReportResponse {

    private final InfractionReportRequest infractionReportRequest;

    public com.picpay.banking.pix.core.domain.infraction.InfractionReport toDomain() {
        return com.picpay.banking.pix.core.domain.infraction.InfractionReport.builder()
            .infractionReportId(infractionReportRequest.getId())
            .infractionType(InfractionType.resolve(infractionReportRequest.getInfractionType().getValue()))
            .reportedBy(infractionReportRequest.getReportedBy())
            .transactionId(infractionReportRequest.getTransactionId())
            .details(infractionReportRequest.getReportDetails())
            .ispbDebited(Integer.valueOf(infractionReportRequest.getDebitedParticipant()))
            .ispbCredited(Integer.valueOf(infractionReportRequest.getCreditedParticipant()))
            .dateCreate(infractionReportRequest.getCreationTime())
            .dateLastUpdate(infractionReportRequest.getLastModified())
            .build();
    }
}
