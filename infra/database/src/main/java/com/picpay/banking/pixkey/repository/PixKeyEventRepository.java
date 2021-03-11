package com.picpay.banking.pixkey.repository;

import com.picpay.banking.pixkey.entity.PixKeyEventEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PixKeyEventRepository extends PagingAndSortingRepository<PixKeyEventEntity, Long> {
}
