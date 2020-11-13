package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListPixKeyRequestDTO {

    private Integer ispb;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nrAgencia;
    private Integer tpConta;
    private String nrConta;
    private Integer tpPessoa;
    private Long cpfCnpj;
}
