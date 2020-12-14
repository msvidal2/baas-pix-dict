/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.common.entity;

import com.picpay.banking.pix.core.domain.Execution;
import com.picpay.banking.pix.core.domain.ExecutionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 11/12/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "job_execution")
public class ExecutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String exitMessage;
    @Enumerated(EnumType.STRING)
    private ExecutionType type;

    public static ExecutionEntity from(Execution execution) {
        return ExecutionEntity.builder()
            .startTime(execution.getStartTime())
            .endTime(execution.getEndTime())
            .exitMessage(execution.getExitMessage())
            .type(execution.getType())
            .build();
    }

    public Execution to() {
        return Execution.builder()
            .startTime(this.startTime)
            .endTime(this.endTime)
            .exitMessage(this.exitMessage)
            .type(this.type)
            .build();
    }

}
