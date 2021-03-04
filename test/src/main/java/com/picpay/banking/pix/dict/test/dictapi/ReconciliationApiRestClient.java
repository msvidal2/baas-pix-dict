package com.picpay.banking.pix.dict.test.dictapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ReconciliationApiRestClient {

    private static final String LOG_NAME = "runFullSync: {}";
    private static final RestTemplate restTemplate = new RestTemplateBuilder().build();

    private final String reconciliationUrl;

    public ReconciliationApiRestClient(@Value("${picpay.reconciliation.url}") String reconciliationUrl) {
        this.reconciliationUrl = reconciliationUrl;
    }

    public void runFullSync() {
        var result = restTemplate.postForObject(reconciliationUrl + "sync/full", HttpEntity.EMPTY, String.class);
        log.info(LOG_NAME, result);
    }

    public void runOnlySyncVerifier(final String keyType) {
        var result = restTemplate.postForObject(reconciliationUrl + "sync/onlyVerifier/" + keyType, HttpEntity.EMPTY, String.class);
        log.info(LOG_NAME, result);
    }

    public void runSyncVerifier(final String keyType) {
        var result = restTemplate.postForObject(reconciliationUrl + "sync/full/" + keyType, HttpEntity.EMPTY, String.class);
        log.info(LOG_NAME, result);
    }

}
