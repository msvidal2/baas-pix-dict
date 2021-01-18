package com.picpay.banking.reconciliation.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.domain.DictEvent;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent;
import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent.ReconciliationSyncOperation;
import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
import com.picpay.banking.pixkey.dto.PixKeyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconciliationSyncEventListener {

    private final ObjectMapper objectMapper;
    private final ContentIdentifierEventRecordUseCase contentIdentifierEventRecordUseCase;

    @StreamListener(value = PixKeyEventOutputBinding.OUTPUT,
        condition = "headers['SKIP_RECONCILIATION'] == null ? true: headers['SKIP_RECONCILIATION'] == false")
    public void reconciliationSyncEvent(Message<DictEvent> message) {
        var dictEvent = message.getPayload();
        var pixKeyDTO = objectMapper.convertValue(dictEvent.getData(), PixKeyDTO.class);

        ReconciliationSyncEvent reconciliationEvent;
        switch (dictEvent.getAction()) {
            case ADD:
                reconciliationEvent = buildEvent(pixKeyDTO, ReconciliationSyncOperation.ADD, pixKeyDTO.getCreatedAt(), pixKeyDTO.getOldCid());
                break;
            case EDIT:
                reconciliationEvent = buildEvent(pixKeyDTO, ReconciliationSyncOperation.UPDATE, pixKeyDTO.getUpdatedAt(), pixKeyDTO.getOldCid());
                break;
            case DELETE:
                reconciliationEvent = buildEvent(pixKeyDTO, ReconciliationSyncOperation.REMOVE, pixKeyDTO.getRemovedAt(), pixKeyDTO.getCid());
                break;
            default:
                throw new UnsupportedOperationException("The action attribute is required");
        }

        reconciliationEvent.validate();

        List<ReconciliationEvent> domainEvents = reconciliationEvent.toDomain();
        contentIdentifierEventRecordUseCase.execute(domainEvents.toArray(ReconciliationEvent[]::new));
    }

    private ReconciliationSyncEvent buildEvent(PixKeyDTO pixKeyDTO, ReconciliationSyncOperation operation, LocalDateTime happenedAt, String oldCid) {
        return ReconciliationSyncEvent.builder()
            .key(pixKeyDTO.getKey())
            .keyType(pixKeyDTO.getType())
            .newCid(pixKeyDTO.getCid())
            .operation(operation)
            .happenedAt(happenedAt)
            .oldCid(oldCid)
            .build();
    }

}