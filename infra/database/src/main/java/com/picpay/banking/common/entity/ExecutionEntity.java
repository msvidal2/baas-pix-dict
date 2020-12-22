/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.common.entity;

import com.picpay.banking.pix.core.domain.Execution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 11/12/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TASK_EXECUTION")
public class ExecutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TASK_SEQ")
    @TableGenerator(
        name="TASK_SEQ",
        table="TASK_SEQ",
        pkColumnName = "UNIQUE_KEY",
        valueColumnName = "id",
        pkColumnValue="0",
        allocationSize = 1
    )
    @Column(name = "TASK_EXECUTION_ID")
    private Long taskExecutionId;
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @Column(name = "END_TIME")
    private LocalDateTime endTime;
    @Column(name = "EXIT_MESSAGE")
    private String exitMessage;
    @Column(name = "TASK_NAME")
    private String taskName;
    @Column(name = "LAST_UPDATED")
    private LocalDateTime lastUpdated;
    @Column(name = "EXIT_CODE")
    private Integer exitCode;

    public static ExecutionEntity from(Execution execution) {
        return ExecutionEntity.builder()
            .startTime(execution.getStartTime())
            .endTime(execution.getEndTime())
            .exitMessage(execution.getExitMessage())
            .taskName(execution.getTaskName())
            .exitCode(execution.getExitCode())
            .lastUpdated(execution.getLastUpdated())
            .build();
    }

    public Execution to() {
        return Execution.builder()
            .startTime(this.startTime)
            .endTime(this.endTime)
            .exitMessage(this.exitMessage)
            .taskName(this.taskName)
            .lastUpdated(this.lastUpdated)
            .exitCode(this.exitCode)
            .build();
    }

}
