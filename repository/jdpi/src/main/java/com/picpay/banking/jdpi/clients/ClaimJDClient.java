package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.ListPendingClaimRequestDTO;
import com.picpay.banking.jdpi.dto.request.ClaimCancelRequestDTO;
import com.picpay.banking.jdpi.dto.request.ClaimConfirmationRequestDTO;
import com.picpay.banking.jdpi.dto.request.CompleteClaimRequestDTO;
import com.picpay.banking.jdpi.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.jdpi.dto.request.ListClaimRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimCancelResponseDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindClaimResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListClaimResponseDTO;
import com.picpay.banking.jdpi.fallbacks.ClaimJDClientFallbackFactory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "Claim",
        url = "${pix.services.jdpi.claim.url}",
        path = "/jdpi/dict/api",
        fallbackFactory = ClaimJDClientFallbackFactory.class)
@Headers({
        "Content-Encoding: gzip",
        "Accept-Encoding: gzip"
})
public interface ClaimJDClient {

    @PostMapping("/v1/reivindicacao/incluir")
    ClaimResponseDTO createClaim(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                 @RequestBody CreateClaimRequestDTO dto);

    @PostMapping("/v1/reivindicacao/{claimId}/confirmar")
    ClaimResponseDTO confirmation(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                              @PathVariable String claimId,
                                              @RequestBody ClaimConfirmationRequestDTO dto);

    @PostMapping("/v1/reivindicacao/{claimId}/cancelar")
    ClaimCancelResponseDTO cancel(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                  @PathVariable String claimId,
                                  @RequestBody ClaimCancelRequestDTO dto);

    @PostMapping("/v1/reivindicacao/listar/pendentes")
    ListClaimResponseDTO listPending(@RequestHeader("Chave-Idempotencia") String requestIdentifier, @RequestBody ListPendingClaimRequestDTO dto);

    @PostMapping("/v1/reivindicacao/{claimId}/concluir")
    ClaimResponseDTO complete(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                              @PathVariable String claimId,
                              @RequestBody CompleteClaimRequestDTO dto);

    @PostMapping("/v1/reivindicacao/listar")
    ListClaimResponseDTO list(@RequestHeader("Chave-Idempotencia") String requestIdentifier, @RequestBody ListClaimRequestDTO dto);

    @GetMapping("/v1/reivindicacao/{idReivindicacao}")
    FindClaimResponseDTO find(@PathVariable String idReivindicacao);
}
