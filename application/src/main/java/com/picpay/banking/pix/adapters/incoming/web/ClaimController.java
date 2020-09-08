package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.ClaimCancelDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ClaimConfirmationDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CompleteClaimRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreateClaimRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ListClaimRequestWebDTO;
import com.picpay.banking.pix.converters.CreateClaimWebConverter;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.usecase.ClaimCancelUseCase;
import com.picpay.banking.pix.core.usecase.ClaimConfirmationUseCase;
import com.picpay.banking.pix.core.usecase.CompleteClaimUseCase;
import com.picpay.banking.pix.core.usecase.CreateClaimUseCase;
import com.picpay.banking.pix.core.usecase.FindClaimUseCase;
import com.picpay.banking.pix.core.usecase.ListClaimUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    private CreateClaimWebConverter converter;

    @ApiOperation(value = "Create a new Claim.")
    @PostMapping
    @ResponseStatus(CREATED)
    public Claim create(@RequestBody @Validated CreateClaimRequestWebDTO requestDTO) {
        Claim claim = converter.convert(requestDTO);

        return createAddressKeyUseCase.createClaim(claim, requestDTO.getRequestIdentifier());
    }

    @ApiOperation("Confirm an addressing key claim")
    @PostMapping("/{claimId}/confirm")
    public Claim confirm(@PathVariable String claimId, @RequestBody @Validated ClaimConfirmationDTO dto) {

        var claim = Claim.builder()
            .claimId(claimId)
            .ispb(dto.getIspb()).build();

        return claimConfirmationUseCase.confirm(claim,
                dto.getReason(),
                dto.getRequestIdentifier());
    }

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

        return listClaimUseCase.listClaimUseCase(claim, requestDTO.isPending(), requestDTO.getLimit(), requestIdentifier);
    }

    @ApiOperation("Cancel an addressing key claim")
    @DeleteMapping("/{claimId}")
    public Claim cancel(@PathVariable String claimId, @RequestBody @Validated ClaimCancelDTO dto) {
        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(dto.getIspb())
                .build();

        return claimCancelUseCase.cancel(claim, dto.isCanceledClaimant(), dto.getReason(), dto.getRequestIdentifier());
    }

    @ApiOperation("Complete an aaddressing key claim")
    @PutMapping("/{claimId}/complete")
    public Claim complete(@PathVariable String claimId, @RequestBody @Validated CompleteClaimRequestWebDTO dto) {
        return completeClaimUseCase.complete(Claim.builder()
                        .claimId(claimId)
                        .ispb(dto.getIspb())
                        .build(),
                dto.getRequestIdentifier());
    }

    @ApiOperation(value = "Find Claim by Claim Id.")
    @GetMapping("/{claimId}")
    @ResponseStatus(HttpStatus.OK)
    public Claim find(@PathVariable String claimId) {
        var claim = Claim.builder()
                .claimId(claimId)
                .build();

        return findClaimUseCase.findClaimUseCase(claim);
    }

}
