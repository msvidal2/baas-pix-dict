package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.RequestSyncFileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/sync", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class ReconciliationController {

    private final RequestSyncFileUseCase requestSyncFileUseCase;

    private final FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase;

    @Trace
    @GetMapping("/{keyType}")
    public ResponseEntity<ContentIdentifierFile> updateAccount(@PathVariable("keyType")KeyType keyType) {
        final var contentIdentifierFile = this.requestSyncFileUseCase.execute(keyType);
        return ResponseEntity.ok(contentIdentifierFile);
    }

}
