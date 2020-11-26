/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.clients;

import com.picpay.banking.pixkey.dto.request.CreateEntryRequest;
import com.picpay.banking.pixkey.dto.response.CreateEntryResponse;
import com.picpay.banking.pixkey.dto.response.GetEntryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "PixKey",
        url = "${pix.bacen.dict.entries.url}",
        path = "/v1")
public interface BacenSyncClient {

    @PostMapping(value = "/entries",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    CreateEntryResponse createPixKey(@RequestBody CreateEntryRequest createEntryRequest);

    @GetMapping(value = "/entries/{key}",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    GetEntryResponse findPixKey(
            @RequestHeader("PI-RequestingParticipant") String requestingParticipant,
            @RequestHeader("PI-PayerId") String payerId,
            @RequestHeader("PI-EndToEndId") String endToEndId,
            @PathVariable("key") String picKey
    );

}
