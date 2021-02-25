package com.picpay.banking.pixkey.repository;
/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyIdEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PixKeyRepository extends PagingAndSortingRepository<PixKeyEntity,PixKeyIdEntity>, JpaSpecificationExecutor<PixKeyEntity> {

    Optional<PixKeyEntity> findByIdKeyAndDonatedAutomaticallyTrue(String key);

    Optional<PixKeyEntity> findByIdKeyAndReasonNot(String key, Reason inactivity);

    @Query("SELECT t FROM pix_key t " +
        "WHERE t.participant = :participant " +
        "   AND t.branch = :branch " +
        "   AND t.accountNumber = :accountNumber " +
        "   AND t.accountType = :accountType" +
        "   AND t.reason = :reason")
    List<PixKeyEntity> findByAccountAndReasonNot(Integer participant,
                                                                 String branch,
                                                                 String accountNumber,
                                                                 AccountType accountType,
                                                                 Reason reason);

    Optional<PixKeyEntity> findByCidAndReasonNot(String cid, Reason inactivity);

    Page<PixKeyEntity> findAllByIdTypeAndReasonNot(KeyType keyType, Reason inactivity, Pageable pageable);

    @Query(value = "select lower(hex(bit_xor(UNHEX(p.cid)))) from pix_key p where p.type = :keyType and p.reason != 'INACTIVITY'", nativeQuery = true)
    @Transactional(readOnly = true)
    String computeVsync(@Param("keyType") String keyType);

}

