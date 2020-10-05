package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDErrorDTO {

    @JsonAlias({"Codigo", "codigo"})
    private String code;

    @JsonAlias({"Mensagem", "mensagem"})
    private String message;

    @JsonAlias({"Erros", "erros"})
    private List<JDFieldErroDTO> errors;

    @JsonAlias({"idCorrelacao"})
    private String correlationId;

}
