package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.pix.original.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.fallbacks.ClaimClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "claimClient",
        url = "${pix.services.original.url}",
        path = "/dict/v1/claims",
        fallbackFactory = ClaimClientFallbackFactory.class)
public interface ClaimClient {

    @PostMapping
    ResponseWrapperDTO<ClaimResponseDTO> create(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                @RequestBody CreateClaimRequestDTO createClaimRequestDTO);

}
