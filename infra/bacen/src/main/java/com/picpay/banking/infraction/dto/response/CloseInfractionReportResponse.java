/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.dto.response;

import com.picpay.banking.infraction.dto.request.InfractionReportRequest;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
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
@XmlRootElement(name = "CloseInfractionReportResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CloseInfractionReportResponse {

    @XmlElement(name = "InfractionReport")
    private InfractionReportRequest infractionReportRequest;

    public InfractionReport toInfractionReport() {
        return InfractionReport.builder()
            .infractionType(InfractionType.resolve(this.getInfractionReportRequest().getInfractionType().getValue()))
            .reportedBy(ReportedBy.resolve(this.getInfractionReportRequest().getReportedBy().getValue()))
            .endToEndId(this.getInfractionReportRequest().getTransactionId())
            .details(this.getInfractionReportRequest().getReportDetails())
            .infractionReportId(this.getInfractionReportRequest().getId())
            .situation(InfractionReportSituation.resolve(this.getInfractionReportRequest().getStatus().getValue()))
            .ispbDebited(Integer.parseInt(this.getInfractionReportRequest().getDebitedParticipant()))
            .ispbCredited(Integer.parseInt(this.getInfractionReportRequest().getCreditedParticipant()))
            .dateCreate(this.getInfractionReportRequest().getCreationTime())
            .dateLastUpdate(this.getInfractionReportRequest().getLastModified())
            .analyze(InfractionAnalyze.builder().analyzeResult(
                InfractionAnalyzeResult.resolve(this.infractionReportRequest.getAnalysisResult().getValue()))
                .details(this.infractionReportRequest.getAnalysisDetails()).build())
            .build();
    }

}
