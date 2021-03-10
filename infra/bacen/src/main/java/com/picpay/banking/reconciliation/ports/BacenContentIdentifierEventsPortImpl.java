package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.reconciliation.clients.BacenArqClient;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import com.picpay.banking.reconciliation.dto.request.CidSetFileRequest;
import com.picpay.banking.reconciliation.dto.response.ListCidSetEventsResponse.CidSetEvent;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class BacenContentIdentifierEventsPortImpl implements BacenContentIdentifierEventsPort {


    private final BacenArqClient bacenArqClient;

    private final BacenReconciliationClient bacenReconciliationClient;

    private final String participant;
    private final String urlGateway;

    public BacenContentIdentifierEventsPortImpl(final BacenArqClient bacenArqClient, final BacenReconciliationClient bacenReconciliationClient,
        @Value("${picpay.ispb}") String participant, @Value("${pix.bacen.dict.url}") String urlGateway) {

        this.bacenArqClient = bacenArqClient;
        this.bacenReconciliationClient = bacenReconciliationClient;
        this.participant = participant;
        this.urlGateway = urlGateway;
    }

    @Override
    public Set<BacenCidEvent> list(final KeyType keyType, final LocalDateTime startTime) {
        Set<BacenCidEvent> result = new HashSet<>();

        boolean hasNext = true;
        final int LIMIT = 200;
        AtomicReference<LocalDateTime> nextDate = new AtomicReference<>(startTime);
        while (hasNext) {
            var bacenEvents = bacenReconciliationClient.getEvents(participant, KeyTypeBacen.resolve(keyType).name(), nextDate.get(), LIMIT);

            if (Objects.isNull(bacenEvents.getCidSetEvents())) {
                hasNext = false;
            } else {
                result.addAll(bacenEvents.getCidSetEvents().getEvents().stream()
                    .map(CidSetEvent::toContentIdentifierEvent)
                    .collect(Collectors.toSet()));

                nextDate.set(bacenEvents.getEndTime());
                hasNext = bacenEvents.isHasMoreElements();
            }
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
    public Set<String> downloadCidsFromBacen(final String url) {
        final String REGEX = "^.*\\/\\/[^\\/]+:?[0-9]?\\/";
        final var urlFile = url.replaceAll(REGEX, urlGateway + "/arq/");

        final var file = this.bacenArqClient.request(URI.create(urlFile));
        return Stream.of(file.split("\n")).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    @Override
    public Optional<PixKey> getPixKey(final String cid) {
        try {
            final var response = this.bacenReconciliationClient.getEntryByCid(cid, participant);

            return Optional.of(response.toDomain());
        } catch (FeignException.NotFound ex) {
            log.info("BacenContentIdentifierEventsPort: getEntryByCid not found for cid {}", cid, ex);
            return Optional.empty();
        }
    }

}
