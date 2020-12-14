/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.clients;

import com.picpay.banking.infraction.dto.request.AcknowledgeInfractionReportRequest;
import com.picpay.banking.infraction.dto.request.CancelInfractionReportRequest;
import com.picpay.banking.infraction.dto.request.CloseInfractionReportRequest;
import com.picpay.banking.infraction.dto.request.CreateInfractionReportRequest;
import com.picpay.banking.infraction.dto.response.AcknowledgeInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.CancelInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.CloseInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.CreateInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.ListInfractionReportsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@FeignClient(name = "bacenInfractionClient",
    url = "${pix.bacen.dict.url}",
    path = "dict/api/v1/infraction-reports")
public interface InfractionBacenClient {

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    CreateInfractionReportResponse create(@RequestBody CreateInfractionReportRequest request);

    @PostMapping(value = "/{InfractionReportId}/cancel",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    CancelInfractionReportResponse cancel(
        @RequestBody CancelInfractionReportRequest cancelInfractionReportRequest,
        @PathVariable("InfractionReportId") String infractionReportId);

    @PostMapping(value = "/{InfractionReportId}/close", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    CloseInfractionReportResponse close(@RequestBody CloseInfractionReportRequest request,
                                        @PathVariable("InfractionReportId") String infractionReportId);

    @GetMapping(value = "/",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    ListInfractionReportsResponse listInfractions(@RequestParam(value = "Participant") String ispb,
                                                  @RequestParam(value = "Limit") Integer limit,
                                                  @RequestParam(value = "ModifiedAfter") String modifiedBefore,
                                                  @RequestParam(value = "ModifiedAfter") String modifiedAfter,
                                                  @RequestParam(value = "IncludeDetails") boolean includeDetails);

    @PostMapping(value = "{InfractionReportId}/acknowledge",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    AcknowledgeInfractionReportResponse acknowledge(@PathVariable("InfractionReportId") String infractionReportId,
                                                    @RequestBody AcknowledgeInfractionReportRequest acknowledgeInfractionReportRequest);

}
