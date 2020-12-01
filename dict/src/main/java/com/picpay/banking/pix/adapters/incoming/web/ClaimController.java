package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.claim.dto.response.ClaimResponse;
import com.picpay.banking.pix.adapters.incoming.web.dto.*;
import com.picpay.banking.pix.adapters.incoming.web.dto.response.ClaimResponseDTO;
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

    private ConfirmClaimUseCase confirmClaimUseCase;

    private ClaimCancelUseCase claimCancelUseCase;

    private ListClaimUseCase listClaimUseCase;

    private CompleteClaimUseCase completeClaimUseCase;

    private FindClaimUseCase findClaimUseCase;

    @Trace
    @ApiOperation(value = "Create a new Claim.")
    @PostMapping
    @ResponseStatus(CREATED)
    public ClaimResponseDTO create(@RequestHeader String requestIdentifier,
                                   @RequestBody @Valid CreateClaimRequestWebDTO requestDTO) {

        log.info("Claim_creating",
                kv("requestIdentifier", requestIdentifier),
                kv("Key", requestDTO.getKey()),
                kv("NameIspb", requestDTO.getIspb()),
                kv("AccountNumber", requestDTO.getAccountNumber()),
                kv("BranchNumber", requestDTO.getBranchNumber()));

        var claim = createAddressKeyUseCase.execute(requestDTO.toDomain(), requestIdentifier);

        return ClaimResponseDTO.from(claim);
    }

    @Trace
    @ApiOperation("Confirm an pix key claim")
    @PostMapping("/{claimId}/confirm")
    public Claim confirm(@RequestHeader String requestIdentifier,
                         @PathVariable String claimId,
                         @RequestBody @Validated ClaimConfirmationDTO dto) {

        log.info("Claim_confirming",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimId),
                kv("dto", dto));

        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(dto.getIspb())
                .build();

        return confirmClaimUseCase.execute(claim,
                dto.getReason(),
                requestIdentifier);
    }

    @Trace
    @ApiOperation(value = "List Claim.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ClaimIterable list(@RequestHeader String requestIdentifier,
                              @Valid ListClaimRequestWebDTO requestDTO) {

        log.info("Claim_listing",
                kv("requestIdentifier", requestIdentifier),
                kv("dto", requestDTO));

        var claim = Claim.builder()
            .ispb(requestDTO.getIspb())
            .personType(requestDTO.getPersonType())
            .cpfCnpj(requestDTO.getCpfCnpj())
            .branchNumber(requestDTO.getBranchNumber())
            .accountNumber(requestDTO.getAccountNumber())
            .accountType(requestDTO.getAccountType())
            .build();

        return listClaimUseCase.execute(claim,
                requestDTO.getPending(), requestDTO.getLimit(), requestDTO.getClaim(), requestDTO.getDonor(),
                requestDTO.getStartDateAsLocalDateTime(), requestDTO.getEndDateAsLocalDateTime(),
                requestIdentifier);
    }

    @Trace
    @ApiOperation("Cancel an pix key claim")
    @DeleteMapping("/{claimId}")
    public ClaimResponse cancel(@RequestHeader String requestIdentifier,
                        @PathVariable String claimId,
                        @RequestBody @Validated ClaimCancelDTO dto) {

        log.info("Claim_canceling",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimId),
                kv("dto", dto));

        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(dto.getIspb())
                .build();

        var claimCancelled = claimCancelUseCase.execute(claim, dto.isCanceledClaimant(), dto.getReason(), requestIdentifier);

        return ClaimResponse.from(claim);
    }

    @Trace
    @ApiOperation("Complete an pix key claim")
    @PutMapping("/{claimId}/complete")
    public Claim complete(@RequestHeader String requestIdentifier,
                          @PathVariable String claimId,
                          @RequestBody @Validated CompleteClaimRequestWebDTO dto) {

        log.info("Claim_completing",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimId),
                kv("dto", dto));

        return completeClaimUseCase.execute(Claim.builder()
                        .claimId(claimId)
                        .ispb(dto.getIspb())
                        .build(),
                requestIdentifier);
    }

    @Trace
    @ApiOperation(value = "Find Claim by Claim Id.")
    @GetMapping("/{claimId}")
    @ResponseStatus(HttpStatus.OK)
    public ClaimResponseDTO find(@PathVariable String claimId,
                                 @RequestParam("ispb") String ispb,
                                 @RequestParam("reivindicador") boolean reivindicador) {

        log.info("Claim_finding", kv("claimId", claimId));

        var claim = findClaimUseCase.execute(claimId, ispb, reivindicador);

        return ClaimResponseDTO.from(claim);
    }

}
