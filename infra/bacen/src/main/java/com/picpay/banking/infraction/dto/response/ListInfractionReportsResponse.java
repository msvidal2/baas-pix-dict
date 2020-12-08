/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.dto.response;

import com.picpay.banking.infraction.dto.request.AnalysisResult;
import com.picpay.banking.infraction.dto.request.InfractionReportRequest;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "ListInfractionReportsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListInfractionReportsResponse {

    @XmlElement(name = "HasMoreElements")
    private Boolean hasMoreElements;
    @XmlElement(name = "InfractionReports")
    private List<InfractionReportRequest> infractionReportRequest;

    public static List<InfractionReport> toInfractionReportList(final ListInfractionReportsResponse response) {
        return response.infractionReportRequest
            .stream()
            .map(infractionReport -> InfractionReport.builder()
                     .ispbRequester(ispbRequester(infractionReport))
                     .endToEndId(infractionReport.getTransactionId())
                     .infractionType(InfractionType.resolve(infractionReport.getInfractionType().getValue()))
                     .reportedBy(ReportedBy.resolve(infractionReport.getReportedBy().getValue()))
                     .details(infractionReport.getReportDetails())
                     .dateLastUpdate(infractionReport.getLastModified())
                     .dateCreate(infractionReport.getCreationTime())
                     .ispbCredited(Integer.parseInt(infractionReport.getCreditedParticipant()))
                     .ispbDebited(Integer.parseInt(infractionReport.getDebitedParticipant()))
                     .situation(InfractionReportSituation.resolve(infractionReport.getStatus().getValue()))
                     .analyze(InfractionAnalyze.builder()
                                  .details(infractionReport.getAnalysisDetails())
                                  .analyzeResult(InfractionAnalyzeResult.resolve(infractionReport.getAnalysisResult().getValue()))
                                  .build())
                     .build()
                )
            .collect(Collectors.toList());
    }

    private static int ispbRequester(final InfractionReportRequest infractionReport) {
        if (infractionReport.getReportedBy().equals(ReportedBy.CREDITED_PARTICIPANT))
            return Integer.parseInt(infractionReport.getCreditedParticipant());

        return Integer.parseInt(infractionReport.getDebitedParticipant());
    }

}
