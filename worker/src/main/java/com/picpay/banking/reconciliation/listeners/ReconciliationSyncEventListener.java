package com.picpay.banking.reconciliation.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent;
import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
import com.picpay.banking.pixkey.dto.DictEvent;
import com.picpay.banking.pixkey.dto.PixKeyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconciliationSyncEventListener {

    private final ContentIdentifierEventRecordUseCase contentIdentifierEventRecordUseCase;
    private static final ObjectMapper mapper = new ObjectMapper();

    @StreamListener(PixKeyEventOutputBinding.TOPIC)
    public void reconciliationSyncEvent(Message<DictEvent> message) {
        // TODO: Falta ignorar as mensagens que possuem o PixKey com o atribur reason RECONCILIATION
        var dictEvent = message.getPayload();

        var module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        mapper.registerModule(module);

        var pixKeyDTO = mapper.convertValue(dictEvent.getData(), PixKeyDTO.class);

        ReconciliationSyncEvent.ReconciliationSyncOperation operation = null;
        LocalDateTime happenedAt = null;
        String oldCid = null;
        switch (dictEvent.getAction()) {
            case ADD:
                operation = ReconciliationSyncEvent.ReconciliationSyncOperation.ADD;
                happenedAt = pixKeyDTO.getCreatedAt();
                oldCid = pixKeyDTO.getOldCid();
                break;
            case EDIT:
                operation = ReconciliationSyncEvent.ReconciliationSyncOperation.UPDATE;
                happenedAt = pixKeyDTO.getUpdatedAt();
                oldCid = pixKeyDTO.getOldCid();
                break;
            case DELETE:
                operation = ReconciliationSyncEvent.ReconciliationSyncOperation.REMOVE;
                happenedAt = pixKeyDTO.getRemovedAt();
                oldCid = pixKeyDTO.getCid();
                break;
        }

        var reconciliationEvent = ReconciliationSyncEvent.builder()
            .key(pixKeyDTO.getKey())
            .keyType(pixKeyDTO.getType())
            .operation(operation)
            .newCid(pixKeyDTO.getCid())
            .oldCid(oldCid)
            .happenedAt(happenedAt)
            .build();

        reconciliationEvent.validate();

        List<ReconciliationEvent> domainEvents = reconciliationEvent.toDomain();
        contentIdentifierEventRecordUseCase.execute(domainEvents.toArray(ReconciliationEvent[]::new));
    }

}
