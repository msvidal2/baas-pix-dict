/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.dto.response;

import com.picpay.banking.infraction.dto.request.InfractionReport;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = "CreateInfractionReportResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateInfractionReportResponse {

    @XmlElement(name = "InfractionReport")
    private InfractionReport infractionReport;

    public com.picpay.banking.pix.core.domain.InfractionReport toInfractionReport() {
        return com.picpay.banking.pix.core.domain.InfractionReport.builder()
            .build();

    }

}
