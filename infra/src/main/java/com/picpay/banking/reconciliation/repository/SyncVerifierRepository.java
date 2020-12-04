package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.reconciliation.entity.SyncVerifierEntity;
import org.springframework.data.repository.CrudRepository;

public interface SyncVerifierRepository extends CrudRepository<SyncVerifierEntity, KeyTypeBacen> {

}
