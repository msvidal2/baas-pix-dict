package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
public interface ContentIdentifierRepository extends PagingAndSortingRepository<ContentIdentifierEntity, Integer> {

    Page<ContentIdentifierEntity> findAllByKeyType(KeyType keyType, Pageable pageable);

}
