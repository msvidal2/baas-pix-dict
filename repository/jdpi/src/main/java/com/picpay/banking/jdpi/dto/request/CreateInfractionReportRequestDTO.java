package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInfractionReportRequestDTO {

    @JsonProperty("ispb")
    private int ispbRequester;

    @JsonProperty("endToEndId")
    private String endToEndId;

    @JsonProperty("tpInfracao")
    private int infractionType;

    @JsonProperty("detalhes")
    private String details;

}
