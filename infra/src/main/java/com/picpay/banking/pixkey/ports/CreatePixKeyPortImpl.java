package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.request.CreateEntryRequest;
import com.picpay.banking.pixkey.dto.response.CreateEntryResponse;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-pix-key";
    private final BacenKeyClient bacenKeyClient;
    private final SavePixKeyPort savePixKeyPort;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason) {
        CreateEntryRequest createEntryRequest = CreateEntryRequest.from(pixKey, reason, requestIdentifier);
        CreateEntryResponse response = bacenKeyClient.createPixKey(createEntryRequest);

//        PixKeyEntity entity = savePixKeyPort.save(response.toEntity());
//        return entity.toPixKey();
        return PixKey.builder().build();
    }

    public PixKey fallbackMethod(String requestIdentifier, PixKey pixKey, CreateReason reason, Exception e) {
        log.error("PixKey_creating"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", pixKey.getKey())
                , kv("NameIspb", pixKey.getIspb())
                , kv("AccountNumber", pixKey.getAccountNumber())
                , kv("BranchNumber", pixKey.getBranchNumber()));
        log.error(e.getMessage(), e);
        throw new RuntimeException(e);
    }

}
