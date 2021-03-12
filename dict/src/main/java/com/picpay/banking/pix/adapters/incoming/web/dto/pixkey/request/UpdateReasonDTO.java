package com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request;

import com.picpay.banking.pix.core.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UpdateReasonDTO {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    BRANCH_TRANSFER(Reason.BRANCH_TRANSFER),
    RECONCILIATION(Reason.RECONCILIATION);

    private Reason value;

}
