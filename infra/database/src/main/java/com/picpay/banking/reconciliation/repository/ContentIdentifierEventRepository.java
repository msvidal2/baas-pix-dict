package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEventEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface ContentIdentifierEventRepository extends CrudRepository<ContentIdentifierEventEntity, Integer> {

    Set<ContentIdentifierEventEntity> findByKeyTypeAndKeyOwnershipDateGreaterThanEqual(KeyType keyType, LocalDateTime keyOwnershipDate);

    Optional<ContentIdentifierEventEntity> findByCid(String cid);

}
