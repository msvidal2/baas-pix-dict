package com.picpay.banking.pix.adapters.incoming.web.dto.request.pixkey;

import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CreateReasonDTO {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    RECONCILIATION(Reason.RECONCILIATION);

    private Reason value;

    private KeyValidator validator;

    CreateReasonDTO(Reason reason) {
        this.value = reason;
    }

    public static CreateReasonDTO resolve(Reason reason) {
        return Stream.of(values())
                .filter(v -> v.value.equals(reason))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}
