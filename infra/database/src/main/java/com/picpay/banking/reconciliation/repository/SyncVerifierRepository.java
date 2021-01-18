package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.SyncVerifierEntity;
import org.springframework.data.repository.CrudRepository;

public interface SyncVerifierRepository extends CrudRepository<SyncVerifierEntity, KeyType> {

}
