package com.picpay.banking.common.repository;/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


import com.picpay.banking.common.entity.ExecutionEntity;
import com.picpay.banking.pix.core.domain.ExecutionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionEntity, BigInteger> {

    Optional<ExecutionEntity> findFirstByExitMessageAndTypeOrderByEndTimeDesc(String exitMessage, ExecutionType type);

}
