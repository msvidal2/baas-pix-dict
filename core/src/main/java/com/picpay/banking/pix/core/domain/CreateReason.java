package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.validators.key.KeyValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreateReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST.getValue());

    private Integer value;

    private KeyValidator validator;

    CreateReason(int i) {
        this.value = i;
    }

    public static CreateReason resolve(int value) {
        for(CreateReason reason : values()) {
            if (reason.value == value) {
                return reason;
            }
        }

        return null;
    }
}