/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Table(name = "infraction_report")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfractionReportEntity {

    @Id
    @Column(name = "infraction_report_id")
    private String infractionReportId;
    @Column(name = "end_to_end_id")
    private String endToEndId;
    @Enumerated(EnumType.STRING)
    @Column(name = "reported_by")
    private ReportedBy reportedBy;
    @Enumerated(EnumType.STRING)
    private InfractionReportSituation situation;
    @Column(name = "ispb_debited")
    private String ispbDebited;
    @Column(name = "ispb_credited")
    private String ispbCredited;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "infraction_type")
    private InfractionType infractionType;
    private String details;
    @Enumerated(EnumType.STRING)
    @Column(name = "analyze_result")
    private InfractionAnalyzeResult analyzeResult;
    @Column(name = "analyze_details")
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
            .infractionType(infractionReport.getInfractionType())
            .details(infractionReport.getDetails())
            .analyzeResult(getAnalyzeResult(infractionReport))
            .analyzeDetails(getAnalyzeDetails(infractionReport))
            .build();
    }

    private static String getAnalyzeDetails(final InfractionReport infractionReport) {
        InfractionAnalyze analyze = infractionReport.getAnalyze();
        if (analyze != null) {
            return Optional.ofNullable(analyze.getDetails()).orElse(null);
        }
        return null;
    }

    private static InfractionAnalyzeResult getAnalyzeResult(final InfractionReport infractionReport) {
        InfractionAnalyze analyze = infractionReport.getAnalyze();
        if (analyze != null) {
            return Optional.ofNullable(analyze.getAnalyzeResult()).orElse(null);
        }
        return null;
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
            .infractionType(infractionReportEntity.getInfractionType())
            .details(infractionReportEntity.getDetails())
            .analyze(Optional.ofNullable(InfractionAnalyze.builder().analyzeResult(infractionReportEntity.getAnalyzeResult())
                                             .details(infractionReportEntity.getAnalyzeDetails()).build()).orElse(null)).build();
    }

}
