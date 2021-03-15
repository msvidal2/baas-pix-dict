package com.picpay.banking.pix.core.domain.infraction.events;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class InfractionAnalyzeEventData implements Serializable {

    private InfractionAnalyzeResult analyzeResult;
    private String details;

}
