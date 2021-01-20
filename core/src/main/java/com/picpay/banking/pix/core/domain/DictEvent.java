package com.picpay.banking.pix.core.domain;

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
    private DictAction action;
    private Object data;

    public enum Domain {
        KEY,
        CLAIM,
        INFRACTION
    }

}
