package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.adapters.incoming.web.dto.*;
import com.picpay.banking.pix.adapters.incoming.web.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.converters.CreateClaimWebConverter;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.usecase.claim.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.CREATED;

@Api(value = "Claim")
@RestController
@RequestMapping(value = "v1/claims", produces = "application/json")
@AllArgsConstructor
@Slf4j
public class ClaimController {

    private CreateClaimUseCase createAddressKeyUseCase;

    private ClaimConfirmationUseCase claimConfirmationUseCase;

    private ClaimCancelUseCase claimCancelUseCase;

    private ListClaimUseCase listClaimUseCase;

    private CompleteClaimUseCase completeClaimUseCase;

    private FindClaimUseCase findClaimUseCase;

    @Trace
    @ApiOperation(value = "Create a new Claim.")
    @PostMapping
    @ResponseStatus(CREATED)
    public ClaimResponseDTO create(@RequestBody @Valid CreateClaimRequestWebDTO requestDTO) {
        log.info("Claim_creating"
                , kv("key", requestDTO.getKey())
                , kv("Ispb", requestDTO.getIspb())
                , kv("accountNumber", requestDTO.getAccountNumber())
                , kv("branchNumber", requestDTO.getBranchNumber()));

        var claim = createAddressKeyUseCase.execute(requestDTO.toDomain(), requestDTO.getRequestIdentifier());

        return ClaimResponseDTO.from(claim);
    }

    @Trace
    @ApiOperation("Confirm an pix key claim")
    @PostMapping("/{claimId}/confirm")
    public Claim confirm(@PathVariable String claimId, @RequestBody @Validated ClaimConfirmationDTO dto) {
        log.info("Claim_confirm"
                , kv("claimId", claimId)
                , kv("ispb", dto.getIspb())
                , kv("requestIdentifier", dto.getRequestIdentifier()));

        var claim = Claim.builder()
            .claimId(claimId)
            .ispb(dto.getIspb()).build();

        return claimConfirmationUseCase.execute(claim,
                dto.getReason(),
                dto.getRequestIdentifier());
    }

    @Trace
    @ApiOperation(value = "List Claim.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ClaimIterable list(ListClaimRequestWebDTO requestDTO, @RequestHeader String requestIdentifier) {
        log.info("Claim_list"
                , kv("accountType", requestDTO.getAccountType())
                , kv("branchNumber", requestDTO.getBranchNumber())
                , kv("ispb", requestDTO.getIspb())
                , kv("requestIdentifier", requestIdentifier));

        var claim = Claim.builder()
            .ispb(requestDTO.getIspb())
            .personType(requestDTO.getPersonType())
            .cpfCnpj(requestDTO.getCpfCnpj())
            .branchNumber(requestDTO.getBranchNumber())
            .accountNumber(requestDTO.getAccountNumber())
            .accountType(requestDTO.getAccountType())
            .build();

        return listClaimUseCase.execute(claim, requestDTO.isPending(), requestDTO.getLimit(), requestIdentifier);
    }

    @Trace
    @ApiOperation("Cancel an pix key claim")
    @DeleteMapping("/{claimId}")
    public Claim cancel(@PathVariable String claimId, @RequestBody @Validated ClaimCancelDTO dto) {
        log.info("Claim_cancel"
                , kv("claimId", claimId)
                , kv("ispb", dto.getIspb())
                , kv("requestIdentifier", dto.getRequestIdentifier()));

        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(dto.getIspb())
                .build();

        return claimCancelUseCase.execute(claim, dto.isCanceledClaimant(), dto.getReason(), dto.getRequestIdentifier());
    }

    @Trace
    @ApiOperation("Complete an pix key claim")
    @PutMapping("/{claimId}/complete")
    public Claim complete(@PathVariable String claimId, @RequestBody @Validated CompleteClaimRequestWebDTO dto) {
        log.info("Claim_complete"
                , kv("claimId", claimId)
                , kv("ispb", dto.getIspb())
                , kv("requestIdentifier", dto.getRequestIdentifier()));

        return completeClaimUseCase.execute(Claim.builder()
                        .claimId(claimId)
                        .ispb(dto.getIspb())
                        .build(),
                dto.getRequestIdentifier());
    }

    @Trace
    @ApiOperation(value = "Find Claim by Claim Id.")
    @GetMapping("/{claimId}")
    @ResponseStatus(HttpStatus.OK)
    public Claim find(@PathVariable String claimId) {
        log.info("Claim_find", kv("claimId", claimId));

        return findClaimUseCase.execute(claimId);
    }

}
