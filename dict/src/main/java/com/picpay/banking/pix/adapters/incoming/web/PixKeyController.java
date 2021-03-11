package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.response.ListKeyResponseWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request.ListPixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request.RemovePixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request.UpdateAccountPixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request.CreatePixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.response.PixKeyResponseDTO;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import com.picpay.banking.pix.core.usecase.pixkey.FindPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.ListPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.PixKeyEventRegistryUseCase;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;
import com.picpay.banking.pix.core.validators.pixkey.CreatePixKeyValidator;
import com.picpay.banking.pix.core.validators.pixkey.RemovePixKeyValidator;
import com.picpay.banking.pix.core.validators.pixkey.UpdatePixKeyValidator;
import com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive;
import com.picpay.banking.pix.infra.openapi.msg.PixKeyControllerMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = PixKeyControllerMessages.CLASS_CONTROLLER)
@RestController
@RequestMapping(value = "v1/keys", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@UnavailableWhileSyncIsActive
public class PixKeyController {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";

    private final FindPixKeyUseCase findPixKeyUseCase;
    private final ListPixKeyUseCase listPixKeyUseCase;
    private final PixKeyEventRegistryUseCase pixKeyPixKeyEventRegistryUseCase;

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_CREATE)
    @PostMapping
    @ResponseStatus(ACCEPTED)
    @ValidateIdempotency(CreatePixKeyRequestWebDTO.class)
    public PixKeyResponseDTO create(@IdempotencyKey @RequestHeader String requestIdentifier,
                                    @RequestBody @Validated @NotNull CreatePixKeyRequestWebDTO requestDTO) {

        //TODO temporario.
        if (requestDTO != null && requestDTO.getPersonType() != null && requestDTO.getPersonType().getValue() == 0)
            requestDTO.setFantasyName(null);

        var pixKey = requestDTO.toPixKey();
        var reason = requestDTO.getReason().getValue();

        CreatePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        log.info("PixKey_creating",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("key", requestDTO.getKey() != null ? requestDTO.getKey() : null),
                kv("NameIspb", requestDTO.getIspb()),
                kv("AccountNumber", requestDTO.getAccountNumber()),
                kv("BranchNumber", requestDTO.getBranchNumber()));

        pixKeyPixKeyEventRegistryUseCase.execute(PixKeyEvent.PENDING_CREATE,
                requestIdentifier,
                pixKey,
                reason);

        return PixKeyResponseDTO.from(requestDTO.toPixKey());
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_LIST)
    @GetMapping
    @ResponseStatus(OK)
    public List<ListKeyResponseWebDTO> list(@RequestHeader String requestIdentifier,
                                            @Valid ListPixKeyRequestWebDTO requestDTO) {

        log.info("PixKey_listing",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("NameIspb", requestDTO.getIspb()),
                kv("AccountNumber", requestDTO.getAccountNumber()),
                kv("BranchNumber", requestDTO.getBranchNumber()));

        return ListKeyResponseWebDTO.from(listPixKeyUseCase.execute(requestIdentifier, requestDTO.toDomain()));
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_FIND)
    @GetMapping("/{key}")
    @ResponseStatus(OK)
    public PixKeyResponseDTO find(@RequestHeader String requestIdentifier,
                                  @PathVariable String key,
                                  @RequestHeader String userId) {

        log.info("PixKey_finding",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("key", key),
                kv("userId", userId));

        return PixKeyResponseDTO.from(findPixKeyUseCase.execute(requestIdentifier, key, userId));
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_DELETE)
    @DeleteMapping("{key}")
    @ResponseStatus(ACCEPTED)
    public PixKeyResponseDTO remove(@RequestHeader String requestIdentifier,
                       @PathVariable String key,
                       @RequestBody @Validated RemovePixKeyRequestWebDTO dto) {

        var pixKey = dto.toDomain(key);
        var reason = dto.getReason().getValue();

        RemovePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        log.info("PixKey_removing",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("key", key),
                kv("dto", dto));

        pixKeyPixKeyEventRegistryUseCase.execute(PixKeyEvent.PENDING_REMOVE,
                requestIdentifier,
                pixKey,
                reason);

        return PixKeyResponseDTO.from(pixKey);
    }

    @Trace
    @ApiOperation(value = PixKeyControllerMessages.METHOD_UPDATE_ACCOUNT)
    @PutMapping("{key}")
    @ResponseStatus(ACCEPTED)
    @ValidateIdempotency(UpdateAccountPixKeyRequestWebDTO.class)
    public PixKeyResponseDTO updateAccount(@IdempotencyKey @RequestHeader String requestIdentifier,
                                           @PathVariable String key,
                                           @RequestBody @Validated UpdateAccountPixKeyRequestWebDTO dto) {
        var pixKey = dto.toDomain(key);
        var reason = dto.getReason().getValue();

        UpdatePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        log.info("PixKey_updatingAccount",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("key", key),
                kv("dto", dto));


        pixKeyPixKeyEventRegistryUseCase.execute(PixKeyEvent.PENDING_UPDATE,
                requestIdentifier,
                pixKey,
                reason);

        return PixKeyResponseDTO.from(pixKey);
    }
}
