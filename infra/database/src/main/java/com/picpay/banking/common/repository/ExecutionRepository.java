/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.common.repository;

import com.picpay.banking.common.entity.ExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionEntity, BigInteger> {

    Optional<ExecutionEntity> findFirstByExitMessageAndTaskNameOrderByEndTimeDesc(String exitMessage, String taskName);

}
