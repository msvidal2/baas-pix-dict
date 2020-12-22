package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.reconciliation.entity.SyncVerifierHistoricActionEntity;
import org.springframework.data.repository.CrudRepository;

public interface SyncVerifierHistoricActionRepository extends CrudRepository<SyncVerifierHistoricActionEntity, Integer> {

}
