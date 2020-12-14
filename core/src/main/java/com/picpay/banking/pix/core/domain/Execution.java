/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author rafael.braga
 * @version 1.0 11/12/2020
 */
@Builder
@Getter
@ToString
@AllArgsConstructor
public class Execution {

    private static final String SUCCESS = "SUCCESS";
    private static final int MAXIMUM_SIZE = 2499;
    private static final int START = 0;

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String exitMessage;
    private final ExecutionType type;

    public static Execution success(LocalDateTime startTime, LocalDateTime endTime, ExecutionType type) {
        return Execution.builder()
            .type(type)
            .endTime(endTime)
            .exitMessage(SUCCESS)
            .startTime(startTime)
            .build();
    }

    public static Execution fail(LocalDateTime startTime, LocalDateTime endTime, ExecutionType type, Exception e) {
        Optional<String> message = Optional.ofNullable(e.getMessage());
        var finalMessage = message.map(mes -> StringUtils.substring(mes, START, MAXIMUM_SIZE)).orElse("FAILED");

        return Execution.builder()
            .type(type)
            .endTime(endTime)
            .exitMessage(finalMessage)
            .startTime(startTime)
            .build();
    }


}
