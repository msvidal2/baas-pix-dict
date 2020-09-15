package com.picpay.banking.pix.original.dto.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OriginalFieldErroDTO {

    private String uniqueId;

    private String informationCode;

    private String message;
}
