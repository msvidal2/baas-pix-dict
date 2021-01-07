package com.picpay.banking.reconciliation.listeners;

import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent;
import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
import com.picpay.banking.reconciliation.config.ReconciliationEventsInputBinding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconciliationSyncEventListener {

    private final ContentIdentifierEventRecordUseCase contentIdentifierEventRecordUseCase;

    @StreamListener(ReconciliationEventsInputBinding.INPUT)
    public void reconciliationSyncEvent(Message<ReconciliationSyncEvent> message) {
        //        log.info("new_reconciliation_sync_event_received",
        //            kv("msg_id", message.getHeaders().getId()),
        //            kv("Headers", message.getHeaders()),
        //            kv("partition", message.getHeaders().get("partition")),
        //            kv("claimId", claim.getClaimId()));
    }

}
