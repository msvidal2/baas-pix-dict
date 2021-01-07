package com.picpay.banking.pix.core.ports.pixkey.picpay;

import com.picpay.banking.pix.core.domain.PixKey;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReconciliationSyncEventPort {

    void eventByPixKeyUpdated(Optional<PixKey> oldPixKey, PixKey newPixKey);

    void eventByPixKeyCreated(PixKey pixKey);

    void eventByPixKeyRemoved(Optional<PixKey> pixKey, LocalDateTime removedAt);

}
