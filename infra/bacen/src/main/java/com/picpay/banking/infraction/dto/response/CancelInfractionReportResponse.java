package com.picpay.banking.infraction.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.infraction.dto.request.InfractionReportRequest;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 27/11/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CancelInfractionReportResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CancelInfractionReportResponse {

    @XmlElement(name = "ResponseTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    @XmlElement(name = "InfractionReport")
    private InfractionReportRequest infractionReport;

    public InfractionReport toDomain() {
        return InfractionReport.builder()
            .endToEndId(infractionReport.getTransactionId())
            .infractionType(InfractionType.resolve(infractionReport.getInfractionType().getValue()))
            .reportedBy(ReportedBy.resolve(infractionReport.getReportedBy().getValue()))
            .details(infractionReport.getReportDetails())
            .infractionReportId(infractionReport.getId())
            .situation(InfractionReportSituation.resolve(infractionReport.getStatus().getValue()))
            .ispbDebited(infractionReport.getDebitedParticipant())
            .ispbCredited(infractionReport.getCreditedParticipant())
            .dateCreate(infractionReport.getCreationTime())
            .dateLastUpdate(infractionReport.getLastModified())
            .build();
    }

}
