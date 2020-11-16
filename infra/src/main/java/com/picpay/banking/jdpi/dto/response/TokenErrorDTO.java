package com.picpay.banking.jdpi.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenErrorDTO {

    private int code;
    private String error;
    private String message;

}
