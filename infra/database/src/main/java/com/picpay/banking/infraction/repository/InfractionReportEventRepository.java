package com.picpay.banking.infraction.repository;

import com.picpay.banking.infraction.entity.InfractionReportEventEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfractionReportEventRepository extends PagingAndSortingRepository<InfractionReportEventEntity, Long> {
}
