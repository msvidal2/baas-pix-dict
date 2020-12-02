package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
public interface ContentIdentifierRepository extends PagingAndSortingRepository<ContentIdentifierEntity, String> {

    @EntityGraph(attributePaths = "pixKey")
    List<ContentIdentifierEntity> findAllByKeyType(KeyTypeBacen keyType);

    @EntityGraph(attributePaths = "pixKey")
    Optional<ContentIdentifierEntity> findByKey(String key);

}
