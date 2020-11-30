package com.picpay.banking.reconciliation.repository;

import com.picpay.banking.pixkey.dto.request.KeyType;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.ContentIdentifierFile.StatusContentIdentifierFile;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
public interface ContentIdentifierFileRepository extends CrudRepository<ContentIdentifierFileEntity, Integer> {

    Optional<ContentIdentifierFileEntity> findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(KeyType keyType, List<StatusContentIdentifierFile> statusContentIdentifierFile);

}
