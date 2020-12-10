package com.picpay.banking.pix.sync.repositories;

import com.picpay.banking.pix.sync.entities.ContentIdentifierEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContentIdentifierEventRepository extends JpaRepository<ContentIdentifierEventEntity, String> {

    @Query("")
    ContentIdentifierEventEntity findByCid();

    @Query("")
    ContentIdentifierEventEntity listByDateTime();
}
