package com.picpay.banking.mockserver.dict;

import com.picpay.banking.mockserver.config.ClientAndServerInstance;
import com.picpay.banking.mockserver.dict.reconciliation.SyncVerificationsNOK;
import com.picpay.banking.mockserver.dict.reconciliation.SyncVerificationsOK;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static org.mockserver.model.HttpRequest.request;

@Slf4j
@Service
@AllArgsConstructor
public class ReconciliationMockServer {

    private SyncVerificationsOK syncVerificationsOK;
    private SyncVerificationsNOK syncVerificationsNOK;

    @PostConstruct
    public void start() {
        ClientAndServerInstance.get().when(
            request()
                .withMethod("POST")
                .withPath("/api/v1/sync-verifications/"))
            .respond(this::syncVerifications);
    }

    private HttpResponse syncVerifications(final HttpRequest httpRequest) throws IOException {
        syncVerificationsOK.setNextChain(syncVerificationsNOK);
        return syncVerificationsOK.run(httpRequest);
    }

}
