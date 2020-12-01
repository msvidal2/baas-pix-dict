/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.client;

import com.picpay.banking.config.FeignXmlConfig;
import com.picpay.banking.infraction.dto.request.CreateInfractionReportRequest;
import com.picpay.banking.infraction.dto.response.CancelInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.CreateInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.GetInfractionReportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@FeignClient(name = "bacenInfractionClient",
        url = "${pix.bacen.dict.url}",
        path = "/v1/infraction-reports")
public interface CreateInfractionBacenClient {

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    CreateInfractionReportResponse create(@RequestBody CreateInfractionReportRequest request);

    @GetMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    GetInfractionReportResponse find(@RequestParam String infractionReportId,
                                     @RequestHeader(name = "PI-RequestingParticipant") String pIRequestingParticipant);

    @PostMapping(value = "/{InfractionReportId}/cancel",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    CancelInfractionReportResponse cancel(
            @RequestBody CancelInfractionReportRequest cancelInfractionReportRequest,
            @PathVariable("InfractionReportId") String InfractionReportId
    );

    @PostMapping(value = "/{InfractionReportId}/close", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    CloseInfractionReportResponse close(@RequestBody CloseInfractionReportRequest request, @PathVariable("InfractionReportId") String infractionReportId);

}
