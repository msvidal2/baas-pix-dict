package com.picpay.banking.claim.clients;

import com.picpay.banking.claim.dto.request.CreateClaimRequest;
import com.picpay.banking.claim.dto.request.ListClaimsRequest;
import com.picpay.banking.claim.dto.response.CreateClaimResponse;
import com.picpay.banking.claim.dto.response.ListClaimsResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "Claim",
        url = "${pix.bacen.dict.entries.url}",
        path = "/v1")
@Headers({
        "Content-Encoding: gzip",
        "Accept-Encoding: gzip"
})
public interface BacenClaimClient {

    @PostMapping("/claims")
    CreateClaimResponse createClaim(@RequestBody CreateClaimRequest createClaimRequest);

    @GetMapping("/claims")
    ListClaimsResponse listClaims(@SpringQueryMap ListClaimsRequest params);

}
