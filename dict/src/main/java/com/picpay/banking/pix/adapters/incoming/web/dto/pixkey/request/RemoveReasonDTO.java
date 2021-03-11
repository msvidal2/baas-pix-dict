package com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request;

import com.picpay.banking.pix.core.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemoveReasonDTO {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    INACTIVITY(Reason.INACTIVITY),
    FRAUD(Reason.FRAUD),
    RECONCILIATION(Reason.RECONCILIATION);

    private Reason value;

}
