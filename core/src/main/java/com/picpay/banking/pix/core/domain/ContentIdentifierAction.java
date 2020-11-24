package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContentIdentifierAction {

    private ActionType actionType;
    private ContentIdentifier contentIdentifier;

    public enum ActionType {
        ADD,
        REMOVE
    }

}
