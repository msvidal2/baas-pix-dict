package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.*;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.response.ClaimIterableResponseDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.response.ClaimResponseDTO;
import com.picpay.banking.pix.core.events.ClaimEventType;
import com.picpay.banking.pix.core.usecase.claim.*;
import com.picpay.banking.pix.core.validators.claim.ClaimCancelValidator;
import com.picpay.banking.pix.core.validators.claim.CompleteClaimValidator;
import com.picpay.banking.pix.core.validators.claim.ConfirmClaimValidator;
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

    private final ListClaimUseCase listClaimUseCase;
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

        claimEventRegistryUseCase.execute(
                requestIdentifier,
                ClaimEventType.CREATE_PENDING,
                claim);

        return ClaimResponseDTO.from(claim);
    }

    @Trace
    @ApiOperation("Confirm an pix key claim")
    @PostMapping("/{claimId}/confirm")
    @ResponseStatus(ACCEPTED)
    public ClaimResponseDTO confirm(@RequestHeader String requestIdentifier,
                         @PathVariable String claimId,
                         @RequestBody @Validated ClaimConfirmationDTO dto) {

        log.info("Claim_confirming",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(CLAIM_ID, claimId),
                kv("dto", dto));

        var claim = dto.toDomain(claimId);

        ConfirmClaimValidator.validate(claim, claim.getConfirmationReason(), requestIdentifier);

        claimEventRegistryUseCase.execute(requestIdentifier,
                ClaimEventType.CONFIRM_PENDING,
                claim);

        return ClaimResponseDTO.from(claim);
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
    @ResponseStatus(ACCEPTED)
    public ClaimResponseDTO cancel(@RequestHeader String requestIdentifier,
                        @PathVariable String claimId,
                        @RequestBody @Validated ClaimCancelDTO dto) {

        log.info("Claim_canceling",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(CLAIM_ID, claimId),
                kv("dto", dto));

        var claim = dto.toClaim().withClaimId(claimId);
        ClaimCancelValidator.validate(claim, requestIdentifier);

        claimEventRegistryUseCase.execute(
                requestIdentifier,
                ClaimEventType.CANCEL_PENDING,
                claim);

        return ClaimResponseDTO.from(claim);
    }

    @Trace
    @ApiOperation("Complete an pix key claim")
    @PutMapping("/{claimId}/complete")
    @ResponseStatus(ACCEPTED)
    public ClaimResponseDTO complete(@RequestHeader String requestIdentifier,
                          @PathVariable String claimId,
                          @RequestBody @Validated CompleteClaimRequestWebDTO dto) {

        log.info("Claim_completing",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(CLAIM_ID, claimId),
                kv("dto", dto));

        var claim = dto.toClaim().withClaimId(claimId);
        CompleteClaimValidator.validate(requestIdentifier, claim);

        claimEventRegistryUseCase.execute(
                requestIdentifier,
                ClaimEventType.COMPLETE_PENDING,
                claim);

        return ClaimResponseDTO.from(claim);
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
