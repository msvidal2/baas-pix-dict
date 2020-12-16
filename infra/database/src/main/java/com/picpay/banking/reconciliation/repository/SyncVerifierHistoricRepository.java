package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.reconciliation.entity.SyncVerifierHistoricEntity;
import org.springframework.data.repository.CrudRepository;

public interface SyncVerifierHistoricRepository extends CrudRepository<SyncVerifierHistoricEntity, Integer> {

}
