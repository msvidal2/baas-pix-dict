package com.picpay.banking.mockserver.util;

import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

public interface MockServerSteps {

    void setNextChain(MockServerSteps nextChain);

    HttpResponse run(HttpRequest httpRequest);

}
