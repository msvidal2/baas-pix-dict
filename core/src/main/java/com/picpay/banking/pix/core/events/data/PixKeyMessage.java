package com.picpay.banking.pix.core.events.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.PixKeyEvent;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Data
@JsonInclude(NON_NULL)
public class PixKeyMessage {

    private PixKeyEvent event;
    private PixKeyEventData data;
    private String requestIdentifier;
    private Reason reason;
    private ErrorEvent error;

}