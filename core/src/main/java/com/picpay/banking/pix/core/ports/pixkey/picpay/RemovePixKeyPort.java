package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.PixKey;

import java.util.Optional;

public interface RemovePixKeyPort {

    Optional<PixKey> remove(String pixKey, Integer participant);

    void removeByCid(String cid);

}
