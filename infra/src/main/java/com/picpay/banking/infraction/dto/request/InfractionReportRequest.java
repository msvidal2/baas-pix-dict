/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.dto.request;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.infraction.dto.response.Status;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Builder
@Getter
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class InfractionReportRequest {

    @XmlElement(name = "TransactionId")
    private String transactionId;
    @XmlElement(name = "InfractionType")
    private InfractionType infractionType;
    @XmlElement(name = "ReportedBy")
    private ReportedBy reportedBy;
    @XmlElement(name = "ReportDetails")
    private String reportDetails;
    @XmlElement(name = "Id")
    private String id;
    @XmlElement(name = "Status")
    private Status status;
    @XmlElement(name = "DebitedParticipant")
    private String debitedParticipant;
    @XmlElement(name = "CreditedParticipant")
    private String creditedParticipant;
    @XmlElement(name = "CreationTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime creationTime;
    @XmlElement(name = "LastModified")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastModified;

}
