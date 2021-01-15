package com.picpay.banking.pixkey.dto;

import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DictEvent {

    private static final String CONTEXT = "baas_pix";
    private Domain domain;
    private Action action;
    private Object data;

    public enum Domain {
        KEY,
        CLAIM,
        INFRACTION
    }

    public enum Action {
        ADD,
        EDIT,
        DELETE;
    }

}
