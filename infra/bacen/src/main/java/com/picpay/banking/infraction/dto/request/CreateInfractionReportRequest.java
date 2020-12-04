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
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CreateInfractionReportRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateInfractionReportRequest {

    @XmlElement(name = "Participant")
    private int participant;

    @XmlElement(name = "InfractionReport")
    private InfractionReportRequest infractionReportRequest;

    public static CreateInfractionReportRequest from(InfractionReport infractionReport) {
        return CreateInfractionReportRequest
            .builder()
            .participant(infractionReport.getIspbRequester())
            .infractionReportRequest(InfractionReportRequest.builder()
                                  .infractionType(InfractionType.from(infractionReport.getInfractionType()))
                                  .reportDetails(infractionReport.getDetails())
                                  .transactionId(infractionReport.getEndToEndId())
                                  .build())
            .build();
    }


}
