package com.picpay.banking.pix.core.events.data;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfractionAnalyzeEventData implements Serializable {

    private InfractionAnalyzeResult analyzeResult;
    private String details;

    public static InfractionAnalyzeEventData from(InfractionAnalyze infractionAnalyze) {
        return InfractionAnalyzeEventData.builder()
            .analyzeResult(infractionAnalyze.getAnalyzeResult())
            .details(infractionAnalyze.getDetails())
            .build();
    }

}
