/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.clients;

import com.picpay.banking.reconciliation.dto.request.CidSetFileRequest;
import com.picpay.banking.reconciliation.dto.request.CreateSyncVerificationRequest;
import com.picpay.banking.reconciliation.dto.response.CidSetFileResponse;
import com.picpay.banking.reconciliation.dto.response.CreateSyncVerificationResponse;
import com.picpay.banking.reconciliation.dto.response.EntryByCidResponse;
import com.picpay.banking.reconciliation.dto.response.GetCidSetFileResponse;
import com.picpay.banking.reconciliation.dto.response.ListCidSetEventsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;

@FeignClient(value = "Reconcilliation",
    url = "${pix.bacen.url}",
    path = "/dict/api/v1/")
public interface BacenReconciliationClient {

    @PostMapping(value = "/cids/files",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    CidSetFileResponse requestCidFile(CidSetFileRequest cidSetFileRequest);

    @GetMapping(value = "/cids/files/{id}",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    GetCidSetFileResponse getCidFile(@PathVariable("id") Integer id, @RequestHeader("PI-RequestingParticipant") String participant);

    @GetMapping(value = "/cids/entries/{cid}",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    EntryByCidResponse getEntryByCid(@PathVariable("cid") String cid, @RequestHeader("PI-RequestingParticipant") String participant);

    @GetMapping(value = "/cids/events",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    ListCidSetEventsResponse getEvents(
        @RequestParam(value = "Participant") String participant,
        @RequestParam(value = "KeyType") String keyType,
        @RequestParam(value = "StartTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") ZonedDateTime startTime,
        @RequestParam(value = "Limit", defaultValue = "200") Integer limit);

    @PostMapping(value = "/sync-verifications",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    CreateSyncVerificationResponse syncVerifications(CreateSyncVerificationRequest createSyncVerificationRequest);

}
