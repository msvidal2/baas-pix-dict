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
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BacenContentIdentifierEventsPortImpl implements BacenContentIdentifierEventsPort {

    private final BacenArqClient bacenArqClient;

    private final BacenReconciliationClient bacenReconciliationClient;

    private final String participant;
    private final String urlGateway;

    private RateLimiter listCidSetEventsRateLimiter;
    private RateLimiter getEntryByCidRateLimiter;

    public BacenContentIdentifierEventsPortImpl(final BacenArqClient bacenArqClient, final BacenReconciliationClient bacenReconciliationClient,
        @Value("${picpay.ispb}") String participant, @Value("${pix.bacen.dict.url}") String urlGateway) {
        this.bacenArqClient = bacenArqClient;
        this.bacenReconciliationClient = bacenReconciliationClient;
        this.participant = participant;
        this.urlGateway = urlGateway;

        createListCidSetEventsFaultTolerance();
        createGetEntryByCidFaultTolerance();
    }

    private void createListCidSetEventsFaultTolerance() {
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .limitForPeriod(20)
            .timeoutDuration(Duration.ofMinutes(1))
            .build();

        this.listCidSetEventsRateLimiter = RateLimiterRegistry.of(rateLimiterConfig).rateLimiter("listCidSetEvents");
    }

    private void createGetEntryByCidFaultTolerance() {
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .limitForPeriod(3600)
            .timeoutDuration(Duration.ofMinutes(1))
            .build();

        this.getEntryByCidRateLimiter = RateLimiterRegistry.of(rateLimiterConfig).rateLimiter("getEntryByCidRateLimiter");
    }

    @Override
    public Set<BacenCidEvent> list(final KeyType keyType, final LocalDateTime startTime) {
        Set<BacenCidEvent> result = new HashSet<>();

        boolean hasNext = true;
        AtomicReference<LocalDateTime> nextDate = new AtomicReference<>(startTime);
        while (hasNext) {
            listCidSetEventsRateLimiter.acquirePermission();
            var bacenEvents = bacenReconciliationClient.getEvents(participant, KeyTypeBacen.resolve(keyType).name(), nextDate.get(), 200);

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
    public List<String> downloadCidsFromBacen(final String url) {
        final var urlFile = url.replaceAll("^.*\\/\\/[^\\/]+:?[0-9]?\\/", urlGateway + "/arq/");

        final var file = this.bacenArqClient.request(URI.create(urlFile));
        return Stream.of(file.split("\n")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    @Override
    public Optional<PixKey> getPixKey(final String cid) {
        try {
            this.getEntryByCidRateLimiter.acquirePermission();
            final var response = this.bacenReconciliationClient.getEntryByCid(cid, participant);

            return Optional.of(response.toDomain());
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }

}
