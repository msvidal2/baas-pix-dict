package com.picpay.banking.pix.core.domain.infraction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
public class InfractionAnalyze implements Serializable {

    private InfractionAnalyzeResult analyzeResult;
    private String details;


}
