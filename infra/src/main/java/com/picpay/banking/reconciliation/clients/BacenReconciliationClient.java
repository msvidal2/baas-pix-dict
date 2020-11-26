/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.clients;

import com.picpay.banking.reconciliation.dto.request.CidSetFileRequest;
import com.picpay.banking.reconciliation.dto.response.CidSetFileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "Reconcilliation",
        url = "${pix.bacen.url}",
        path = "/")
public interface BacenReconciliationClient {

    @PostMapping(value = "/cids/files",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    CidSetFileResponse requestCid(CidSetFileRequest cidSetFileRequest);



}
