package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InfractionAnalyzeDTO {

    private InfractionAnalyzeResult analyzeResult;
    private String details;


    public static InfractionAnalyzeDTO from(final InfractionAnalyze analyze) {
        if(analyze == null) return null;
        return InfractionAnalyzeDTO.builder().analyzeResult(analyze.getAnalyzeResult()).details(analyze.getDetails()).build();
    }

}
