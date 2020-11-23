package com.picpay.banking.mockserver.dict.reconciliation;

import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;

public interface SyncVerifications {

    void setNextChain(SyncVerifications nextChain);

    HttpResponse run(HttpRequest httpRequest) throws IOException;

}
