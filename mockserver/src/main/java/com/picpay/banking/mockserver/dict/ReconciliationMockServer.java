package com.picpay.banking.mockserver.dict;

import com.picpay.banking.mockserver.dict.reconciliation.events.ListCidEvents;
import com.picpay.banking.mockserver.dict.reconciliation.sync.SyncVerificationsNOK;
import com.picpay.banking.mockserver.dict.reconciliation.sync.SyncVerificationsOK;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static org.mockserver.model.HttpRequest.request;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReconciliationMockServer {

    private final ClientAndServer clientAndServer;
    private final SyncVerificationsOK syncVerificationsOK;
    private final SyncVerificationsNOK syncVerificationsNOK;
    private final ListCidEvents listCidEvents;

    @PostConstruct
    public void start() {
        clientAndServer.when(
            request()
                .withMethod("POST")
                .withPath("/api/v1/sync-verifications/"))
            .respond(this::syncVerifications);

        clientAndServer.when(
            request()
                .withMethod("GET")
                .withPath("/api/v1/cids/events"))
            .respond(this::events);
    }

    private HttpResponse syncVerifications(final HttpRequest httpRequest) throws IOException {
        syncVerificationsOK.setNextChain(syncVerificationsNOK);
        return syncVerificationsOK.run(httpRequest);
    }

    private HttpResponse events(final HttpRequest httpRequest) {
        return listCidEvents.run(httpRequest);
    }

}
