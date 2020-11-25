/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.dto.response;

import com.picpay.banking.infraction.dto.request.InfractionReportRequest;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = "CreateInfractionReportResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateInfractionReportResponse {

    @XmlElement(name = "InfractionReport")
    private InfractionReportRequest infractionReportRequest;

    public static InfractionReport toInfractionReport(CreateInfractionReportResponse response) {
        return InfractionReport.builder()
            .transactionId(response.getInfractionReportRequest().getTransactionId())
            .infractionType(InfractionType.resolve(response.getInfractionReportRequest().getInfractionType().getValue()))
            .reportedBy(ReportedBy.resolve(response.getInfractionReportRequest().getReportedBy().getValue()))
            .details(response.getInfractionReportRequest().getReportDetails())
            .infractionReportId(response.getInfractionReportRequest().getId())
            .situation(InfractionReportSituation.resolve(response.getInfractionReportRequest().getStatus().getValue()))
            .ispbDebited(Integer.parseInt(response.getInfractionReportRequest().getDebitedParticipant()))
            .ispbCredited(Integer.parseInt(response.getInfractionReportRequest().getCreditedParticipant()))
            .dateCreate(response.getInfractionReportRequest().getCreationTime())
            .dateLastUpdate(response.getInfractionReportRequest().getLastModified())
            .build();
    }

}
