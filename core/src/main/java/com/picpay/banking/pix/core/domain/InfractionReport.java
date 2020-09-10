package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class InfractionReport {

    private String infractionReportId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private String ispbDebited;
    private String ispbCredited;
    private LocalDateTime dateCreate;
    private LocalDateTime dateLastUpdate;
    private String ispbRequester;
    private String endToEndId;
    private InfractionType type;
    private String details;
    private String requestIdentifier;
}
