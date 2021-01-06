package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.reconciliation.entity.SyncVerifierEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SyncVerifierRepository extends CrudRepository<SyncVerifierEntity, KeyType> {

    @Query(value = "select lower(hex(bit_xor(UNHEX(k.cid)))) " +
                   "from (select p.cid from pix_key p where p.type = ? union all select '0000000000000000000000000000000000000000000000000000000000000000') k",
        nativeQuery = true)
    String calculateVsync(String type);

}
