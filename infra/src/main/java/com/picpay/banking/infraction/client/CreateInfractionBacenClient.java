/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.client;

import com.picpay.banking.infraction.dto.request.CreateInfractionReportRequest;
import com.picpay.banking.infraction.dto.response.CreateInfractionReportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@FeignClient(name = "bacenInfractionClient",
    url = "${pix.bacen.dict.infraction.url}",
    path = "/api/v1/infraction-reports")
public interface CreateInfractionBacenClient {

    @PostMapping
    CreateInfractionReportResponse create(@RequestBody CreateInfractionReportRequest request);

}
