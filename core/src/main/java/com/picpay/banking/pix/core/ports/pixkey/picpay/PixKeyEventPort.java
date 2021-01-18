package com.picpay.banking.pix.core.ports.pixkey.picpay;

import com.picpay.banking.pix.core.domain.PixKey;

import java.time.LocalDateTime;

public interface PixKeyEventPort {

    void pixKeyWasCreated(PixKey pixKey);

    void pixKeyWasCreatedByReconciliation(PixKey pixKey);

    void pixKeyWasEdited(PixKey oldPixKey, PixKey newPixKey);

    void pixKeyWasEditedByReconciliation(PixKey oldPixKey, PixKey newPixKey);

    void pixKeyWasDeleted(PixKey pixKey, LocalDateTime removeAt);

    void pixKeyWasDeletedByReconciliation(PixKey pixKey, LocalDateTime removeAt);

}
