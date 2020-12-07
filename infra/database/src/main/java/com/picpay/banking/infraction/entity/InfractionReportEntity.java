/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.entity;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
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
import java.util.Optional;

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
    @Column(name = "infraction_report_id")
    private String infractionReportId;
    private String endToEndId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private int ispbDebited;
    private int ispbCredited;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
    private int ispbRequester;
    private InfractionType infractionType;
    private String details;
    private String requestIdentifier;
    private InfractionAnalyzeResult analyzeResult;
    private String analyzeDetails;


    public static InfractionReportEntity fromDomain(InfractionReport infractionReport) {
        return InfractionReportEntity.builder()
            .infractionReportId(infractionReport.getInfractionReportId())
            .endToEndId(infractionReport.getEndToEndId())
            .reportedBy(infractionReport.getReportedBy())
            .situation(infractionReport.getSituation())
            .ispbDebited(infractionReport.getIspbDebited())
            .ispbCredited(infractionReport.getIspbCredited())
            .createdDate(infractionReport.getDateCreate())
            .lastUpdatedDate(infractionReport.getDateLastUpdate())
            .ispbRequester(infractionReport.getIspbRequester())
            .infractionType(infractionReport.getInfractionType())
            .details(infractionReport.getDetails())
            .requestIdentifier(infractionReport.getRequestIdentifier())
            .analyzeResult(Optional.ofNullable(infractionReport.getAnalyze().getAnalyzeResult()).orElse(null))
            .analyzeDetails(Optional.ofNullable(infractionReport.getAnalyze().getDetails()).orElse(null))
            .build();
    }

    public static InfractionReport toDomain(InfractionReportEntity infractionReportEntity) {
        return InfractionReport.builder()
            .infractionReportId(infractionReportEntity.getInfractionReportId())
            .endToEndId(infractionReportEntity.getEndToEndId())
            .reportedBy(infractionReportEntity.getReportedBy())
            .situation(infractionReportEntity.getSituation())
            .ispbDebited(infractionReportEntity.getIspbDebited())
            .ispbCredited(infractionReportEntity.getIspbCredited())
            .dateCreate(infractionReportEntity.getCreatedDate())
            .dateLastUpdate(infractionReportEntity.getLastUpdatedDate())
            .ispbRequester(infractionReportEntity.getIspbRequester())
            .infractionType(infractionReportEntity.getInfractionType())
            .details(infractionReportEntity.getDetails())
            .requestIdentifier(infractionReportEntity.getRequestIdentifier())
            .analyze(Optional.ofNullable(InfractionAnalyze.builder().analyzeResult(infractionReportEntity.getAnalyzeResult())
                .details(infractionReportEntity.getAnalyzeDetails()).build()).orElse(null)).build();
    }

}
