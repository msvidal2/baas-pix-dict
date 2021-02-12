package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.SyncBacenCidEventsEntity;
import org.springframework.data.repository.CrudRepository;

public interface SyncBacenCidEventsRepository extends CrudRepository<SyncBacenCidEventsEntity, KeyType> {

}
