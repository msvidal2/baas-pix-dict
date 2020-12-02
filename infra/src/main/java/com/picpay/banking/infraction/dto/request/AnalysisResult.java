/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.infraction.dto.request;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AnalysisResult {

    AGREED(0),
    DISAGREED(1);

    private final int value;

    public static AnalysisResult from(InfractionAnalyzeResult infractionAnalyzeResult) {
        return Stream.of(values())
            .filter(analysisResult ->  infractionAnalyzeResult.getValue() == analysisResult.value)
            .findFirst()
            .orElse(DISAGREED);
    }
}
