package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ListAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.RemoveAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.UpdateAccountAddressingKeyDTO;
import com.picpay.banking.pix.converters.CreateAddressingKeyWebConverter;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.usecase.CreateAddressingKeyUseCase;
import com.picpay.banking.pix.core.usecase.FindAddressKeyUseCase;
import com.picpay.banking.pix.core.usecase.ListAddressKeyUseCase;
import com.picpay.banking.pix.core.usecase.RemoveAddressingKeyUseCase;
import com.picpay.banking.pix.core.usecase.UpdateAccountAddressingKeyUseCase;
import com.picpay.banking.pix.infra.openapi.msg.AddressingKeyControllerMessages;
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

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Api(value = AddressingKeyControllerMessages.CLASS_CONTROLLER)
@RestController
@RequestMapping(value = "v1/addressing-keys", produces = "application/json")
@AllArgsConstructor
public class AddressingKeyController {

    private CreateAddressingKeyUseCase createAddressingKeyUseCase;

    private RemoveAddressingKeyUseCase removeAddressingKeyUseCase;

    private FindAddressKeyUseCase findAddressKeyUseCase;

    private UpdateAccountAddressingKeyUseCase updateAccountUseCase;

    private ListAddressKeyUseCase listAddressKeyUseCase;

    private CreateAddressingKeyWebConverter converter;

    @ApiOperation(value = AddressingKeyControllerMessages.METHOD_CREATE)

    @PostMapping
    @ResponseStatus(CREATED)
    public AddressingKey create(@RequestBody @Validated CreateAddressingKeyRequestWebDTO requestDTO) {
        AddressingKey addressingKey = converter.convert(requestDTO);

        return createAddressingKeyUseCase.createAddressKeyUseCase(
                addressingKey, requestDTO.getReason(), requestDTO.getRequestIdentifier());
    }

    @ApiOperation(value = AddressingKeyControllerMessages.METHOD_LIST)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<AddressingKey> list(
            @RequestHeader String requestIdentifier, ListAddressingKeyRequestWebDTO requestDTO) {

        AddressingKey addressingKey = AddressingKey.builder()
            .cpfCnpj(Long.valueOf(requestDTO.getCpfCnpj()))
            .personType(requestDTO.getPersonType())
            .accountNumber(requestDTO.getAccountNumber())
            .accountType(requestDTO.getAccountType())
            .branchNumber(requestDTO.getBranchNumber())
            .ispb(requestDTO.getIspb())
            .build();

        return listAddressKeyUseCase.listAddressKeyUseCase(requestIdentifier, addressingKey);
    }

    @ApiOperation(value = AddressingKeyControllerMessages.METHOD_FIND)
    @GetMapping("/{key}")
    @ResponseStatus(HttpStatus.OK)
    public AddressingKey find(@PathVariable String key, @RequestHeader String userId) {
        AddressingKey addressingKey = AddressingKey.builder()
                .key(key)
                .build();

        return findAddressKeyUseCase.findAddressKeyUseCase(addressingKey, userId);
    }

    @ApiOperation(value = AddressingKeyControllerMessages.METHOD_DELETE)
    @DeleteMapping("{key}")
    @ResponseStatus(NO_CONTENT)
    public void remove(@PathVariable String key, @RequestBody @Validated RemoveAddressingKeyRequestWebDTO dto) {
        var addressingKey = AddressingKey.builder()
                .key(key)
                .ispb(dto.getIspb())
                .type(dto.getType())
                .build();

        removeAddressingKeyUseCase.remove(addressingKey, dto.getReason(), dto.getRequestIdentifier());
    }

    @ApiOperation(value = AddressingKeyControllerMessages.METHOD_UPDATE_ACCOUNT)
    @PutMapping("{key}")
    public AddressingKey updateAccount(
            @PathVariable String key, @RequestBody @Validated UpdateAccountAddressingKeyDTO dto) {
        var addressingKey = AddressingKey.builder()
                .key(key)
                .type(dto.getType())
                .ispb(dto.getIspb())
                .branchNumber(dto.getBranchNumber())
                .accountType(dto.getAccountType())
                .accountNumber(dto.getAccountNumber())
                .accountOpeningDate(dto.getAccountOpeningDate())
                .build();

        updateAccountUseCase.update(addressingKey, dto.getReason(), dto.getRequestIdentifier());

        return findAddressKeyUseCase.findAddressKeyUseCase(addressingKey, dto.getUserId());
    }
}
