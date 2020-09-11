package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.*;
import com.picpay.banking.pix.converters.CreatePixKeyWebConverter;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.usecase.*;
import com.picpay.banking.pix.infra.openapi.msg.PixKeyControllerMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Api(value = PixKeyControllerMessages.CLASS_CONTROLLER)
@RestController
@RequestMapping(value = "v1/keys", produces = "application/json")
@AllArgsConstructor
public class PixKeyController {

    private CreatePixKeyUseCase createPixKeyUseCase;

    private RemovePixKeyUseCase removePixKeyUseCase;

    private FindPixKeyUseCase findPixKeyUseCase;

    private UpdateAccountPixKeyUseCase updateAccountUseCase;

    private ListPixKeyUseCase listPixKeyUseCase;

    private CreatePixKeyWebConverter converter;

    @ApiOperation(value = PixKeyControllerMessages.METHOD_CREATE)
    @PostMapping
    @ResponseStatus(CREATED)
    public PixKey create(@RequestBody @Validated CreatePixKeyRequestWebDTO requestDTO) {
        var pixKey = converter.convert(requestDTO);

        return createPixKeyUseCase.createAddressKeyUseCase(
                pixKey, requestDTO.getReason(), requestDTO.getRequestIdentifier());
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_LIST)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ListKeyResponseWebDTO> list(
            @RequestHeader String requestIdentifier, @Valid ListPixKeyRequestWebDTO requestDTO) {

        var pixKey = PixKey.builder()
            .taxId(requestDTO.getCpfCnpj())
            .personType(requestDTO.getPersonType())
            .accountNumber(requestDTO.getAccountNumber())
            .accountType(requestDTO.getAccountType())
            .branchNumber(requestDTO.getBranchNumber())
            .ispb(requestDTO.getIspb())
            .build();

        return converter.convert(listPixKeyUseCase.listAddressKeyUseCase(requestIdentifier, pixKey));
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_FIND)
    @GetMapping("/{key}")
    @ResponseStatus(HttpStatus.OK)
    public PixKey find(@PathVariable String key, @RequestHeader String userId) {
        var pixKey = PixKey.builder()
                .key(key)
                .build();

        return findPixKeyUseCase.findPixKeyUseCase(pixKey, userId);
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_DELETE)
    @DeleteMapping("{key}")
    @ResponseStatus(NO_CONTENT)
    public void remove(@PathVariable String key, @RequestBody @Validated RemovePixKeyRequestWebDTO dto) {
        var pixKey = PixKey.builder()
                .key(key)
                .ispb(dto.getIspb())
                .type(dto.getType())
                .build();

        removePixKeyUseCase.remove(pixKey, dto.getReason(), dto.getRequestIdentifier());
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_UPDATE_ACCOUNT)
    @PutMapping("{key}")
    public PixKey updateAccount(
            @PathVariable String key, @RequestBody @Validated UpdateAccountPixKeyDTO dto) {
        var pixKey = PixKey.builder()
                .key(key)
                .type(dto.getType())
                .ispb(dto.getIspb())
                .branchNumber(dto.getBranchNumber())
                .accountType(dto.getAccountType())
                .accountNumber(dto.getAccountNumber())
                .accountOpeningDate(dto.getAccountOpeningDate())
                .build();

        updateAccountUseCase.update(pixKey, dto.getReason(), dto.getRequestIdentifier());

        return findPixKeyUseCase.findPixKeyUseCase(pixKey, dto.getUserId());
    }
}
