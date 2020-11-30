package com.picpay.banking.pixkey.repository;
/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PixKeyRepository extends JpaRepository<PixKeyEntity, PixKeyIdEntity> {

    Optional<PixKeyEntity> findByIdKey(String key);

    @Query("SELECT t FROM pix_key t " +
            "WHERE t.participant = :participant " +
            "   AND t.branch = :branch " +
            "   AND t.accountNumber = :accountNumber " +
            "   AND t.accountType = :accountType")
    List<PixKeyEntity> findByAccount(Integer participant, String branch, String accountNumber, AccountType accountType);

    void deleteByIdKeyAndParticipant(String key, Integer participant);

    @Query("SELECT t FROM pix_key t " +
            "WHERE t.taxId = :taxId " +
            "   AND t.personType = :personType " +
            "   AND t.branch = :branch " +
            "   AND t.accountNumber = :accountNumber " +
            "   AND t.accountType = :accountType " +
            "   AND t.participant = :participant")
    List<PixKeyEntity> findKeys(String taxId, PersonType personType, String branch, String accountNumber, AccountType accountType, Integer participant);

    @Query("SELECT t FROM pix_key t " +
            "WHERE t.taxId = :taxId " +
            "   AND t.personType = :personType " +
            "   AND t.accountNumber = :accountNumber " +
            "   AND t.accountType = :accountType " +
            "   AND t.participant = :participant")
    List<PixKeyEntity> findKeys(String taxId, PersonType personType, String accountNumber, AccountType accountType, Integer participant);

}
