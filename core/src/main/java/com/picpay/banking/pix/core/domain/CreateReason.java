package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.validators.key.KeyValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreateReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    RECONCILIATION(null); // TODO implementar tipo para reconciliação

    private Reason value;

    private KeyValidator validator;

    CreateReason(Reason reason) {
        this.value = reason;
    }

    public static CreateReason resolve(int value) {
        for(CreateReason reason : values()) {
            if (reason.value.getValue() == value) {
                return reason;
            }
        }

        return null;
    }
}