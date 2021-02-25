package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
public interface ContentIdentifierFileRepository extends CrudRepository<ContentIdentifierFileEntity, Integer> {

    Optional<ContentIdentifierFileEntity> findFirstByKeyTypeOrderByRequestTimeDesc(KeyType keyType);

}
