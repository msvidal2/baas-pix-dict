package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContentIdentifierEventRepository extends CrudRepository<ContentIdentifierEventEntity, Integer> {

    @Query("select e.cid from content_identifier_event e " +
        "where e.keyType = :keyType " +
        "and e.eventOnBacenAt > :synchronizedAt")
    List<String> findAllCidsAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedAt);

    Optional<ContentIdentifierEventEntity> findFirstByKeyOrderByIdDesc(String key);

}
