package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InfractionReport {

    private String infractionReportId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private String ispbDebited;
    private String ispbCredited;
    private String dateCreated;
    private String dateLastUpdated;
    private int ispbRequester;
    private String endToEndId;
    private InfractionType type;
    private String details;
    private String requestIdentifier;
}
