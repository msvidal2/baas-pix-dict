package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.ResultCidFile;
import com.picpay.banking.pix.core.exception.CidFileNotReadyException;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.FetchCidFileCallablePort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.PollCidFilePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class RequestSyncFileUseCase {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private final PollCidFilePort pollCidFilePort;
    private final Integer timeoutPeriod;
    private final Integer pollingAwaitingPeriod;

    public ContentIdentifierFile requestAwaitFile(KeyType keyType) {
        ContentIdentifierFile contentIdentifierFile = request(keyType);
        ResultCidFile resultCidFile = pollCidFile(contentIdentifierFile).orElseThrow(() -> new CidFileNotReadyException(contentIdentifierFile.getId()));
        contentIdentifierFile.addContent(resultCidFile.getCids());
        return contentIdentifierFile;
    }

    private ContentIdentifierFile request(KeyType keyType) {
        log.info("RequestSyncFile_started {}", kv("keyType", keyType));
        final var contentIdentifierFile = this.bacenContentIdentifierEventsPort.requestContentIdentifierFile(keyType);
        this.databaseContentIdentifierPort.saveFile(contentIdentifierFile);
        log.info("RequestSyncFile_ended {} {} ", kv("keyType", keyType), kv("contentIdentifierFileId", contentIdentifierFile.getId()));
        return contentIdentifierFile;
    }

    private Optional<ResultCidFile> pollCidFile(ContentIdentifierFile contentIdentifierFile) {
        var fetchCidFileCallablePort = new FetchCidFileCallablePort(bacenContentIdentifierEventsPort, contentIdentifierFile.getId());
        return pollCidFilePort.poll(fetchCidFileCallablePort, timeoutPeriod, TimeUnit.SECONDS, pollingAwaitingPeriod, TimeUnit.HOURS);
    }

}
