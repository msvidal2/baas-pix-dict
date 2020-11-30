package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.reconciliation.entity.ContentIdentifierActionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
public interface ContentIdentifierActionRepository extends PagingAndSortingRepository<ContentIdentifierActionEntity, Long> {

}
