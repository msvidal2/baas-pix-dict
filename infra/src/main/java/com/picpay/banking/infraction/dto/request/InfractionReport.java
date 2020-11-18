/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import com.picpay.banking.infraction.dto.response.ReportedBy;
import com.picpay.banking.infraction.dto.response.Status;

import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Builder
@Getter
@AllArgsConstructor
public class InfractionReport {

    private final String transactionId;
    private final InfractionType infractionType;
    private final ReportedBy reportedBy;
    private final String reportDetails;
    private final String id;
    private final Status status;
    private final String debitedParticipant;
    private final String creditedParticipant;
    private final LocalDateTime creationTime;
    private final LocalDateTime lastModified;

}
