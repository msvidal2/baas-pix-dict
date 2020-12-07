package com.picpay.banking.mockserver.dict.reconciliation.sync;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.picpay.banking.mockserver.util.MockServerSteps;
import lombok.SneakyThrows;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.MediaType;
import org.springframework.stereotype.Component;

import static org.mockserver.model.HttpResponse.response;

@Component
public class SyncVerificationsOK implements MockServerSteps {

    private MockServerSteps nextChain;

    @Override
    public void setNextChain(final MockServerSteps nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    @SneakyThrows
    public HttpResponse run(final HttpRequest httpRequest) {
        final String result = "OK";

        var xml = new XmlMapper().readTree(httpRequest.getBody().getRawBytes());
        var syncVerification = xml.get("SyncVerification");
        var participantSyncVerifier = syncVerification.get("ParticipantSyncVerifier").asText();
        if (result.equals(participantSyncVerifier)) {
            String participant = syncVerification.get("Participant").asText();
            String keyType = syncVerification.get("KeyType").asText();

            return response()
                .withContentType(MediaType.APPLICATION_XML)
                .withStatusCode(HttpStatusCode.CREATED_201.code())
                .withBody(ReconciliationXMLHelper
                    .createSyncVerificationResponse(participant, keyType, participantSyncVerifier, result));
        } else {
            return nextChain.run(httpRequest);
        }
    }

}
