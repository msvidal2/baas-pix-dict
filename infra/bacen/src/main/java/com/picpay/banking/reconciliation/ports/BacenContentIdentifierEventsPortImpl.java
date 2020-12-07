package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.reconciliation.clients.BacenArqClient;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import com.picpay.banking.reconciliation.dto.request.CidSetFileRequest;
import com.picpay.banking.reconciliation.dto.response.ListCidSetEventsResponse.CidSetEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BacenContentIdentifierEventsPortImpl implements BacenContentIdentifierEventsPort {

    private BacenArqClient bacenArqClient;

    private BacenReconciliationClient bacenReconciliationClient;

    private String participant;
    private String urlGateway;

    public BacenContentIdentifierEventsPortImpl(final BacenArqClient bacenArqClient, final BacenReconciliationClient bacenReconciliationClient,
        @Value("${picpay.ispb}") String participant, @Value("${pix.bacen.url}") String urlGateway) {
        this.bacenArqClient = bacenArqClient;
        this.bacenReconciliationClient = bacenReconciliationClient;
        this.participant = participant;
        this.urlGateway = urlGateway;
    }

    @Override
    public Set<ContentIdentifierEvent> list(final KeyType keyType, final LocalDateTime startTime, final LocalDateTime endTime) {
        Set<ContentIdentifierEvent> result = new HashSet<>();

        boolean hasNext = true;
        LocalDateTime nextDate = startTime;
        while (hasNext) {
            var bacenEvents = bacenReconciliationClient
                .getEvents(participant, KeyTypeBacen.resolve(keyType).name(), nextDate.atZone(ZoneId.of("UTC")), 200);

            result.addAll(bacenEvents.getCidSetEvents().getEvents().stream()
                .map(CidSetEvent::toContentIdentifierEvent)
                .collect(Collectors.toSet()));

            nextDate = bacenEvents.getEndTime();
            hasNext = bacenEvents.isHasMoreElements();
        }

        return result;
    }

    @Override
    public ContentIdentifierFile requestContentIdentifierFile(final KeyType keyType) {

        final var request = CidSetFileRequest.builder()
            .keyType(KeyTypeBacen.resolve(keyType))
            .participant(participant)
            .build();

        final var response = this.bacenReconciliationClient.requestCidFile(request);

        return response.toDomain();
    }

    @Override
    public ContentIdentifierFile getContentIdentifierFileInBacen(final Integer id) {
        final var cidFile = this.bacenReconciliationClient.getCidFile(id, participant);
        return cidFile.toDomain();
    }

    @Override
    public List<String> downloadFile(final String url) {
        final var urlFile = url.replaceAll("^.*\\/\\/[^\\/]+:?[0-9]?\\/", urlGateway + "/arq/");//CHANGE HOST TO GATEWAY

        final var file = this.bacenArqClient.request(URI.create(urlFile));
        return Arrays.asList(file.split("\n"));
    }

}
