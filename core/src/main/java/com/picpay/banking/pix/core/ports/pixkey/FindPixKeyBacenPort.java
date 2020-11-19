package com.picpay.banking.pix.core.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 19/11/20
 */
public interface FindPixKeyBacenPort {

    PixKey findPixKey(String requestIdentifier, String pixKey, String userId);

}
