package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.dto.request.ClaimCompletionRequestDTO;
import com.picpay.banking.pix.original.dto.request.ClaimConfirmationRequestDTO;
import com.picpay.banking.pix.original.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.pix.original.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.fallbacks.ClaimClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "claimClient",
        url = "${pix.services.original.url}",
        path = "/dict/v1/claims",
        fallbackFactory = ClaimClientFallbackFactory.class)
public interface ClaimClient {

    @PostMapping
    ResponseWrapperDTO<ClaimResponseDTO> create(@RequestBody CreateClaimRequestDTO createClaimRequestDTO);

    @PatchMapping("/{id}/confirmations")
    ResponseWrapperDTO<ClaimResponseDTO> confirm(@PathVariable String id,
                                                 @RequestBody ClaimConfirmationRequestDTO claimConfirmationRequestDTO);

    @PatchMapping("/{id}/cancellations")
    ResponseWrapperDTO<ClaimResponseDTO> cancel(@PathVariable String id,
                                                 @RequestBody ClaimConfirmationRequestDTO claimConfirmationRequestDTO);

    @PatchMapping("/{id}/completions")
    ResponseWrapperDTO<ClaimResponseDTO> finish(@PathVariable String id,
                                                @RequestBody ClaimCompletionRequestDTO claimCompletionRequestDTO);

    @GetMapping
    ResponseWrapperDTO<List<ClaimResponseDTO>> find();

}
