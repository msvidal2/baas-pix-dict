package com.picpay.banking.pix.core.domain.infraction;

import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InfractionReport {

    private String infractionReportId;
    private String transactionId;
    @EqualsAndHashCode.Include
    private InfractionType infractionType;
    @EqualsAndHashCode.Include
    private String endToEndId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private int ispbDebited;
    private int ispbCredited;
    private LocalDateTime dateCreate;
    private LocalDateTime dateLastUpdate;
    @EqualsAndHashCode.Include
    private int ispbRequester;
    @EqualsAndHashCode.Include
    private String details;
    private String requestIdentifier;
    private InfractionAnalyze analyze;

}
