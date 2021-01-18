package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.claim.dto.response.ClaimResponse;
import com.picpay.banking.pix.adapters.incoming.web.dto.ClaimCancelDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ClaimConfirmationDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CompleteClaimRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreateClaimRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ListClaimRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.usecase.claim.CancelClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.CompleteClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.ConfirmClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.CreateClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.FindClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.ListClaimUseCase;
import com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.CREATED;

@Api(value = "Claim")
@RestController
@RequestMapping(value = "v1/claims", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
@UnavailableWhileSyncIsActive
public class ClaimController {

    private final CreateClaimUseCase createAddressKeyUseCase;
    private final ConfirmClaimUseCase confirmClaimUseCase;
    private final CancelClaimUseCase cancelClaimUseCase;
    private final ListClaimUseCase listClaimUseCase;
    private final CompleteClaimUseCase completeClaimUseCase;
    private final FindClaimUseCase findClaimUseCase;

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
                .confirmationReason(dto.getReason())
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

        var claimCancelled = cancelClaimUseCase.execute(claim, dto.isCanceledClaimant(), dto.getReason(), requestIdentifier);

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
