package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.adapters.incoming.web.dto.*;
import com.picpay.banking.pix.adapters.incoming.web.dto.response.PixKeyResponseDTO;
import com.picpay.banking.pix.core.usecase.pixkey.*;
import com.picpay.banking.pix.infra.openapi.msg.PixKeyControllerMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Api(value = PixKeyControllerMessages.CLASS_CONTROLLER)
@RestController
@RequestMapping(value = "v1/keys", produces = "application/json")
@AllArgsConstructor
@Slf4j
public class PixKeyController {

    private CreatePixKeyUseCase createPixKeyUseCase;

    private RemovePixKeyUseCase removePixKeyUseCase;

    private FindPixKeyUseCase findPixKeyUseCase;

    private UpdateAccountPixKeyUseCase updateAccountUseCase;

    private ListPixKeyUseCase listPixKeyUseCase;

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_CREATE)
    @PostMapping
    @ResponseStatus(CREATED)
    public PixKeyResponseDTO create(@RequestHeader String requestIdentifier,
                                    @RequestBody @Validated CreatePixKeyRequestWebDTO requestDTO) {

        log.info("PixKey_creating"
                , kv("NameIspb", requestDTO.getIspb())
                , kv("AccountNumber", requestDTO.getAccountNumber())
                , kv("BranchNumber", requestDTO.getBranchNumber()));

        var pixKey = createPixKeyUseCase.execute(
                requestIdentifier,
                requestDTO.toPixKey(),
                requestDTO.getReason());

        return PixKeyResponseDTO.from(pixKey);
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_LIST)
    @GetMapping
    @ResponseStatus(OK)
    public List<ListKeyResponseWebDTO> list(@RequestHeader String requestIdentifier,
                                            @Valid ListPixKeyRequestWebDTO requestDTO) {

        log.info("PixKey_list"
                , kv("requestIdentifier", requestIdentifier)
                , kv("NameIspb", requestDTO.getIspb())
                , kv("AccountNumber", requestDTO.getAccountNumber())
                , kv("BranchNumber", requestDTO.getBranchNumber()));

        return ListKeyResponseWebDTO.from(listPixKeyUseCase.execute(requestIdentifier, requestDTO.toDomain()));
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_FIND)
    @GetMapping("/{key}")
    @ResponseStatus(OK)
    public PixKeyResponseDTO find(@RequestHeader String requestIdentifier,
                                  @PathVariable String key,
                                  @RequestHeader String userId) {

        log.info("PixKey_find"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", key)
                , kv("userId", userId));

        return PixKeyResponseDTO.from(findPixKeyUseCase.execute(requestIdentifier, key, userId));
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_DELETE)
    @DeleteMapping("{key}")
    @ResponseStatus(NO_CONTENT)
    public void remove(@RequestHeader String requestIdentifier,
                       @PathVariable String key,
                       @RequestBody @Validated RemovePixKeyRequestWebDTO dto) {

        log.info("PixKey_remove"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", key)
                , kv("dto", dto));

        removePixKeyUseCase.execute(requestIdentifier, dto.toDomain(key), dto.getReason());
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_UPDATE_ACCOUNT)
    @PutMapping("{key}")
    public PixKeyResponseDTO updateAccount(@RequestHeader String requestIdentifier,
                                           @PathVariable String key,
                                           @RequestBody @Validated UpdateAccountPixKeyRequestWebDTO dto) {
        var pixKey = dto.toDomain(key);

        log.info("PixKey_updateAccount"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", key)
                , kv("dto", dto));

        updateAccountUseCase.execute(requestIdentifier, pixKey, dto.getReason());

        return PixKeyResponseDTO.from(findPixKeyUseCase.execute(requestIdentifier, pixKey.getKey(), dto.getUserId()));
    }
}
