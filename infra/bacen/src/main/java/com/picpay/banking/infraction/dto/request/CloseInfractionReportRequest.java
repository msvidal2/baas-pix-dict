/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.dto.request;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "CloseInfractionReportRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CloseInfractionReportRequest {

    @XmlElement(name = "InfractionReportId")
    private String infractionReportId;

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "AnalysisResult")
    private AnalysisResult analysisResult;

    @XmlElement(name = "AnalysisDetails")
    private String analysisDetails;

    public static CloseInfractionReportRequest from(InfractionReport infractionReport, String ispbPicPay) {
        return CloseInfractionReportRequest.builder()
            .infractionReportId(infractionReport.getInfractionReportId())
            .participant(ispbPicPay)
            .analysisResult(AnalysisResult.from(infractionReport.getAnalyze().getAnalyzeResult()))
            .analysisDetails(infractionReport.getAnalyze().getDetails())
            .build();
    }


}
