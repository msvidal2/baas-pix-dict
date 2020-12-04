package com.picpay.banking.claim.clients;

import com.picpay.banking.claim.dto.request.CompleteClaimRequest;
import com.picpay.banking.claim.dto.request.CreateClaimRequest;
import com.picpay.banking.claim.dto.request.ListClaimsRequest;
import com.picpay.banking.claim.dto.response.CompleteClaimResponse;
import com.picpay.banking.claim.dto.request.ConfirmClaimRequest;
import com.picpay.banking.claim.dto.request.CancelClaimRequest;
import com.picpay.banking.claim.dto.response.ConfirmClaimResponse;
import com.picpay.banking.claim.dto.response.CancelClaimResponse;
import com.picpay.banking.claim.dto.response.CreateClaimResponse;
import com.picpay.banking.claim.dto.response.ListClaimsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "Claim",
        url = "${pix.bacen.dict.entries.url}",
        path = "/v1/claims")
public interface BacenClaimClient {

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    CreateClaimResponse createClaim(@RequestBody CreateClaimRequest createClaimRequest);

    @GetMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    ListClaimsResponse listClaims(@SpringQueryMap ListClaimsRequest request);

    @GetMapping(value = "/{claimId}/confirm",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    ConfirmClaimResponse confirmClaim(@PathVariable("claimId") String claimId,
                                      @RequestBody ConfirmClaimRequest request);

    @PostMapping(value = "/{claimId}/cancel",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    CancelClaimResponse cancel(@PathVariable String claimId, @RequestBody CancelClaimRequest request);

    @PostMapping(value = "/{claimId}/complete",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    CompleteClaimResponse completeClaim(@PathVariable("claimId") String claimId, @RequestBody CompleteClaimRequest completeClaimRequest);

}
