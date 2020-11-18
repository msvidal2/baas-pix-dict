/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.entity;

import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Entity
@Table(name = "infraction_report")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfractionReportEntity {

    @Id
    @Column(name = "id")
    private String infractionReportId;
    private String endToEndId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private int ispbDebited;
    private int ispbCredited;
    private LocalDateTime dateCreate;
    private LocalDateTime dateLastUpdate;
    private int ispbRequester;
    private InfractionType type;
    private String details;
    private String requestIdentifier;
    private InfractionAnalyzeResult analyzeResult;
    private String analyzeDetails;


    public static InfractionReportEntity fromDomain(com.picpay.banking.pix.core.domain.InfractionReport infractionReport) {
        return InfractionReportEntity.builder()
            .infractionReportId(infractionReport.getInfractionReportId())
            .endToEndId(infractionReport.getEndToEndId())
            .reportedBy(infractionReport.getReportedBy())
            .situation(infractionReport.getSituation())
            .ispbDebited(infractionReport.getIspbDebited())
            .ispbCredited(infractionReport.getIspbCredited())
            .dateCreate(infractionReport.getDateCreate())
            .dateLastUpdate(infractionReport.getDateLastUpdate())
            .ispbRequester(infractionReport.getIspbRequester())
            .type(infractionReport.getType())
            .details(infractionReport.getDetails())
            .requestIdentifier(infractionReport.getRequestIdentifier())
            .analyzeResult(infractionReport.getAnalyze().getAnalyzeResult())
            .analyzeDetails(infractionReport.getAnalyze().getDetails())
            .build();
    }


}
