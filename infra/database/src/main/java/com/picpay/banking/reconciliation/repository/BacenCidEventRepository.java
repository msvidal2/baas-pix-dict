package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.BacenCidEventEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BacenCidEventRepository extends CrudRepository<BacenCidEventEntity, Integer> {

    List<BacenCidEventEntity> findByKeyTypeAndEventOnBacenAtAfter(KeyType keyType, LocalDateTime afterAt);

}
