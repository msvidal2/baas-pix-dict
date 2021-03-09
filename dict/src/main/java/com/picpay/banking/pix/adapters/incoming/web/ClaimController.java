package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.adapters.incoming.web.dto.*;
import com.picpay.banking.pix.adapters.incoming.web.dto.response.ClaimIterableResponseDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEvent;
import com.picpay.banking.pix.core.usecase.claim.*;
import com.picpay.banking.pix.core.validators.claim.CreateClaimValidator;
import com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.ACCEPTED;

@Api(value = "Claim")
@RestController
@RequestMapping(value = "v1/claims", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
@UnavailableWhileSyncIsActive
public class ClaimController {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    public static final String CLAIM_ID = "claimId";

    private final ConfirmClaimUseCase confirmClaimUseCase;
    private final CancelClaimUseCase cancelClaimUseCase;
    private final ListClaimUseCase listClaimUseCase;
    private final CompleteClaimUseCase completeClaimUseCase;
    private final FindClaimUseCase findClaimUseCase;

    private final ClaimEventRegistryUseCase claimEventRegistryUseCase;

    @Trace
    @ApiOperation(value = "Create a new Claim.")
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public ClaimResponseDTO create(@RequestHeader String requestIdentifier,
                                   @RequestBody @Valid CreateClaimRequestWebDTO requestDTO) {

        log.info("Claim_creating",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("Key", requestDTO.getKey()),
                kv("NameIspb", requestDTO.getIspb()),
                kv("AccountNumber", requestDTO.getAccountNumber()),
                kv("BranchNumber", requestDTO.getBranchNumber()));

        var claim = requestDTO.toDomain();
        CreateClaimValidator.validate(requestIdentifier, claim);

        claimEventRegistryUseCase.execute(claim, requestIdentifier, ClaimEvent.PENDING_OPEN);

        return ClaimResponseDTO.from(claim);
    }

    @Trace
    @ApiOperation("Confirm an pix key claim")
    @PostMapping("/{claimId}/confirm")
    public ClaimResponseDTO confirm(@RequestHeader String requestIdentifier,
                         @PathVariable String claimId,
                         @RequestBody @Validated ClaimConfirmationDTO dto) {

        log.info("Claim_confirming",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(CLAIM_ID, claimId),
                kv("dto", dto));

        return ClaimResponseDTO.from(confirmClaimUseCase.execute(dto.toDomain(claimId),
                        dto.getReason(),
                        requestIdentifier));
    }

    @Trace
    @ApiOperation(value = "List Claim.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ClaimIterableResponseDTO list(@RequestHeader String requestIdentifier,
                              @Valid ListClaimRequestWebDTO requestDTO) {

        log.info("Claim_listing",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("dto", requestDTO));

        var claim = listClaimUseCase.execute(requestDTO.toDomain(),
                requestDTO.getPending(), requestDTO.getLimit(), requestDTO.getClaimer(),
                requestDTO.getStartDateAsLocalDateTime(), requestDTO.getEndDateAsLocalDateTime(),
                requestIdentifier);

        return ClaimIterableResponseDTO.from(claim);
    }

    @Trace
    @ApiOperation("Cancel an pix key claim")
    @DeleteMapping("/{claimId}")
    public ClaimResponseDTO cancel(@RequestHeader String requestIdentifier,
                        @PathVariable String claimId,
                        @RequestBody @Validated ClaimCancelDTO dto) {

        log.info("Claim_canceling",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(CLAIM_ID, claimId),
                kv("dto", dto));

        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(dto.getIspb())
                .build();

        return ClaimResponseDTO.from(
                cancelClaimUseCase.execute(claim, dto.isCanceledClaimant(), dto.getReason(), requestIdentifier));
    }

    @Trace
    @ApiOperation("Complete an pix key claim")
    @PutMapping("/{claimId}/complete")
    public ClaimResponseDTO complete(@RequestHeader String requestIdentifier,
                          @PathVariable String claimId,
                          @RequestBody @Validated CompleteClaimRequestWebDTO dto) {

        log.info("Claim_completing",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(CLAIM_ID, claimId),
                kv("dto", dto));

        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(dto.getIspb())
                .build();

        return ClaimResponseDTO.from(completeClaimUseCase.execute(claim, requestIdentifier));
    }

    @Trace
    @ApiOperation(value = "Find Claim by Claim Id.")
    @GetMapping("/{claimId}")
    @ResponseStatus(HttpStatus.OK)
    public ClaimResponseDTO find(@PathVariable String claimId,
                                 @RequestParam("ispb") String ispb,
                                 @RequestParam("reivindicador") boolean reivindicador) {

        log.info("Claim_finding", kv(CLAIM_ID, claimId));

        return ClaimResponseDTO.from(findClaimUseCase.execute(claimId, ispb, reivindicador));
    }

}
