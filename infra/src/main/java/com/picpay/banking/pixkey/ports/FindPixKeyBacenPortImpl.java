package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.response.GetEntryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component("FindPixKeyBacenPort")
public class FindPixKeyBacenPortImpl implements FindPixKeyPort {

    private final BacenKeyClient bacenKeyClient;

    @Override
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {
        GetEntryResponse getEntryResponse = bacenKeyClient.findPixKey(pixKey);
        //TODO Ao buscar no Bacen a informação, ela deverá ser incluída no DB?
        return getEntryResponse.toPixKey();
    }

    @Override
    public List<PixKey> findByAccount(String taxId, String branch, String accountNumber, AccountType accountType) {
        return null;
    }

}
