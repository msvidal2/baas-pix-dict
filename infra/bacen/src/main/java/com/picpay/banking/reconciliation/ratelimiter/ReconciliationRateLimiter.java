package com.picpay.banking.reconciliation.ratelimiter;

import com.picpay.banking.reconciliation.dto.response.EntryByCidResponse;
import com.picpay.banking.reconciliation.dto.response.ListCidSetEventsResponse;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.function.Supplier;

@Slf4j
public final class ReconciliationRateLimiter {

    private static ReconciliationRateLimiter instance;
    private RateLimiter getEntryByCidRateLimiter;
    private RateLimiter listCidSetEventsRateLimiter;

    private ReconciliationRateLimiter() {
        createGetEntryByCid();
        createListCidSetEvents();
    }

    public static ReconciliationRateLimiter getInstance() {
        if (instance == null) {
            instance = new ReconciliationRateLimiter();
        }
        return instance;
    }

    private void createGetEntryByCid() {
        final int CIDS_ENTRIES_READ_PER_MINUTE = 3600;
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(CIDS_ENTRIES_READ_PER_MINUTE)
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .timeoutDuration(Duration.ofMinutes(1))
            .build();

        this.getEntryByCidRateLimiter = RateLimiterRegistry.of(rateLimiterConfig).rateLimiter("getEntryByCidRateLimiter");
    }

    private void createListCidSetEvents() {
        final int CIDS_EVENTS_LIST_PER_MINUTE = 20;
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(CIDS_EVENTS_LIST_PER_MINUTE)
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .timeoutDuration(Duration.ofMinutes(1))
            .build();

        this.listCidSetEventsRateLimiter = RateLimiterRegistry.of(rateLimiterConfig).rateLimiter("listCidSetEvents");
    }

    public EntryByCidResponse acquirePermissionForGetEntryByCid(Supplier<EntryByCidResponse> supplier) {
        return getEntryByCidRateLimiter.executeSupplier(supplier);
    }

    public ListCidSetEventsResponse acquirePermissionForListCidSetEvents(Supplier<ListCidSetEventsResponse> supplier) {
        return listCidSetEventsRateLimiter.executeSupplier(supplier);
    }

}
