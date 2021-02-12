package com.picpay.banking.reconciliation.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.domain.DictEvent;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationEvent;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationSyncEvent;
import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
import com.picpay.banking.reconciliation.repository.ContentIdentifierEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconciliationSyncEventListener {

    private final ObjectMapper objectMapper;
    private final ContentIdentifierEventRecordUseCase contentIdentifierEventRecordUseCase;
    private final ContentIdentifierEventRepository contentIdentifierEventRepository;

    @StreamListener(value = PixKeyEventOutputBinding.OUTPUT)
    public void reconciliationSyncEvent(Message<DictEvent> message) {
        var dictEvent = message.getPayload();
        var pixKey = objectMapper.convertValue(dictEvent.getData(), PixKey.class);

        final AtomicReference<String> oldCid = new AtomicReference<>();
        contentIdentifierEventRepository.findFirstByKeyOrderByIdDesc(pixKey.getKey())
            .ifPresent(contentIdentifierEventEntity -> oldCid.set(contentIdentifierEventEntity.getCid()));

        ReconciliationSyncEvent reconciliationEvent = ReconciliationSyncEvent.builder()
            .key(pixKey.getKey())
            .keyType(pixKey.getType())
            .oldCid(oldCid.get())
            .newCid(pixKey.getCid())
            .action(dictEvent.getAction())
            .happenedAt(pixKey.getUpdatedAt())
            .build();

        reconciliationEvent.validate();

        List<ReconciliationEvent> domainEvents = reconciliationEvent.toDomain();
        contentIdentifierEventRecordUseCase.execute(domainEvents.toArray(ReconciliationEvent[]::new));
    }

}