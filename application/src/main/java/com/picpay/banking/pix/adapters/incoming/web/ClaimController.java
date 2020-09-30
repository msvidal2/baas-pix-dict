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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Api(value = "Claim")
@RestController
@RequestMapping(value = "v1/claims", produces = "application/json")
@AllArgsConstructor
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
        var claim = createAddressKeyUseCase.execute(requestDTO.toDomain(), requestDTO.getRequestIdentifier());

        return ClaimResponseDTO.from(claim);
    }

    @Trace
    @ApiOperation("Confirm an pix key claim")
    @PostMapping("/{claimId}/confirm")
    public Claim confirm(@PathVariable String claimId, @RequestBody @Validated ClaimConfirmationDTO dto) {
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
        return findClaimUseCase.execute(claimId);
    }

}
