package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.*;
import com.picpay.banking.pix.adapters.incoming.web.dto.response.PixKeyResponseDTO;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.usecase.pixkey.*;
import com.picpay.banking.pix.infra.openapi.msg.PixKeyControllerMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

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

    @ApiOperation(value = PixKeyControllerMessages.METHOD_CREATE)
    @PostMapping
    @ResponseStatus(CREATED)
    public PixKeyResponseDTO create(@RequestHeader String requestIdentifier,
                                    @RequestBody @Validated CreatePixKeyRequestWebDTO requestDTO) {

        var pixKey = createPixKeyUseCase.execute(
                requestIdentifier,
                requestDTO.toPixKey(),
                requestDTO.getReason());

        return PixKeyResponseDTO.from(pixKey);
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_LIST)
    @GetMapping
    @ResponseStatus(OK)
    public List<ListKeyResponseWebDTO> list(@RequestHeader String requestIdentifier,
                                            @Valid ListPixKeyRequestWebDTO requestDTO) {

        return ListKeyResponseWebDTO.from(listPixKeyUseCase.execute(requestIdentifier, requestDTO.toDomain()));
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_FIND)
    @GetMapping("/{key}")
    @ResponseStatus(OK)
    public PixKeyResponseDTO find(@RequestHeader String requestIdentifier,
                                  @PathVariable String key,
                                  @RequestHeader String userId) {

        var pixKey = PixKey.builder()
                .key(key)
                .build();

        return PixKeyResponseDTO.from(findPixKeyUseCase.execute(requestIdentifier, pixKey, userId));
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_DELETE)
    @DeleteMapping("{key}")
    @ResponseStatus(NO_CONTENT)
    public void remove(@RequestHeader String requestIdentifier,
                       @PathVariable String key,
                       @RequestBody @Validated RemovePixKeyRequestWebDTO dto) {
        removePixKeyUseCase.execute(requestIdentifier, dto.toDomain(key), dto.getReason());
    }

    @ApiOperation(value = PixKeyControllerMessages.METHOD_UPDATE_ACCOUNT)
    @PutMapping("{key}")
    public PixKeyResponseDTO updateAccount(@RequestHeader String requestIdentifier,
                                           @PathVariable String key,
                                           @RequestBody @Validated UpdateAccountPixKeyRequestWebDTO dto) {
        var pixKey = dto.toDomain(key);

        updateAccountUseCase.execute(requestIdentifier, pixKey, dto.getReason());

        return PixKeyResponseDTO.from(findPixKeyUseCase.execute(requestIdentifier, pixKey, dto.getUserId()));
    }
}
