//package com.picpay.banking.pix.dict.cidevents.listeners;
//
//import com.picpay.banking.pix.core.domain.ReconciliationEvent;
//import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class ContentIdentifierEventRecordListener {
//
//    private final ContentIdentifierEventRecordUseCase contentIdentifierEventRecordUseCase;
//
//    @StreamListener("sync-input")
//    public void onMessage(Message<ReconciliationEvent> reconciliationEventMessage) {
//        contentIdentifierEventRecordUseCase.execute(reconciliationEventMessage.getPayload());
//    }
//
//}
