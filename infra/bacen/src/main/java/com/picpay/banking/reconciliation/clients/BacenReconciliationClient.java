/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.clients;

import com.picpay.banking.reconciliation.dto.request.CidSetFileRequest;
import com.picpay.banking.reconciliation.dto.response.CidSetFileResponse;
import com.picpay.banking.reconciliation.dto.response.EntryByCidResponse;
import com.picpay.banking.reconciliation.dto.response.GetCidSetFileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "Reconcilliation",
        url = "${pix.bacen.dict.url}",
        path = "/dict/api/v1/cids")
public interface BacenReconciliationClient {

    @PostMapping(value = "/files",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    CidSetFileResponse requestCidFile(CidSetFileRequest cidSetFileRequest);

    @GetMapping(value = "/files/{id}",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    GetCidSetFileResponse getCidFile(@PathVariable("id") Integer id, @RequestHeader("PI-RequestingParticipant") String participant);

    @GetMapping(value = "/entries/{cid}",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    EntryByCidResponse getEntryByCid(@PathVariable("cid") String cid, @RequestHeader("PI-RequestingParticipant") String participant);


}
