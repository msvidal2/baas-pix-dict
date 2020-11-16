package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountPixKeyRequestDTO {

    @JsonProperty("chave")
    private String key;

    private int ispb;

    @JsonProperty("nrAgenciaNova")
    private String branchNumber;

    @JsonProperty("tpContaNova")
    private int accountType;

    @JsonProperty("nrContaNova")
    private String accountNumber;

    @JsonProperty("dtHrAberturaConta")
    private LocalDateTime accountOpeningDate;

    @JsonProperty("motivo")
    private int reason;

}
