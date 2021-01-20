package com.picpay.banking.pix.core.domain.infraction;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InfractionAnalyze implements Serializable {

    private static final long serialVersionUID = -2375601047270725688L;

    private InfractionAnalyzeResult analyzeResult;
    private String details;


}
