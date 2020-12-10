package com.picpay.banking.mockserver.dict.reconciliation.events;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.picpay.banking.mockserver.util.MockServerSteps;
import lombok.SneakyThrows;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.stereotype.Component;

import java.io.FileReader;

@Component
public class ListCidEvents implements MockServerSteps {


    @Override
    public void setNextChain(final MockServerSteps nextChain) {

    }

    @Override
    @SneakyThrows
    public HttpResponse run(final HttpRequest httpRequest) {
        FileReader evpTxt = new FileReader("src/main/java/com/picpay/banking/mockserver/dict/reconciliation/events/ListCidSetEventsResponse.xml");
        var xml = new XmlMapper().readTree(evpTxt);

        return null;
    }

}
