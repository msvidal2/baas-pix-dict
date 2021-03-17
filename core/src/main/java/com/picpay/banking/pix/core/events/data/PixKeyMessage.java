package com.picpay.banking.pix.core.events.data;

import com.picpay.banking.pix.core.events.PixKeyEvent;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PixKeyMessage {

    private PixKeyEvent event;
    private Object data;
    private int attempts;

}
