package com.picpay.banking.pix.core.domain.infraction;

import com.picpay.banking.pix.core.events.data.InfractionAnalyzeEventData;
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

    public static InfractionAnalyze from(InfractionAnalyzeEventData infractionAnalyzeEventData) {
        return InfractionAnalyze.builder()
            .analyzeResult(infractionAnalyzeEventData.getAnalyzeResult())
            .details(infractionAnalyzeEventData.getDetails())
            .build();
    }

}
