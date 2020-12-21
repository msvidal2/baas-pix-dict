package com.picpay.banking.pix.core.ports.pixkey.picpay;

import com.picpay.banking.pix.core.domain.PixKey;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NotifyReconciliationMessagingPort {

    void notifyPixKeyUpdated(Optional<PixKey> oldPixKey, PixKey newPixKey);

    void notifyPixKeyCreated(PixKey pixKey);

    void notifyPixKeyRemoved(PixKey pixKey, LocalDateTime removedAt);

}
