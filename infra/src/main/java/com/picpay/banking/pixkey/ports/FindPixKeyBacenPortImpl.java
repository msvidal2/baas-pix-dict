package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.response.GetEntryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FindPixKeyBacenPortImpl implements FindPixKeyPort {

    private final BacenKeyClient bacenKeyClient;

    @Override
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {
        GetEntryResponse getEntryResponse = bacenKeyClient.findPixKey(pixKey);
        //TODO Ao buscar no Bacen a informação, ela deverá ser incluída no DB?
        return getEntryResponse.toPixKey();
    }

}
