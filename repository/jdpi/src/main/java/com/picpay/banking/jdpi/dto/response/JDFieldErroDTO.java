package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JDFieldErroDTO {

    @JsonAlias({"Campo", "campo"})
    private String field;

    @JsonAlias({"Mensagens", "mensagens"})
    private List<String> messages;

}
