package com.picpay.banking.pix.dict.test.dictapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class DictApiRestClient {

    private static final String DICT_CREATE_URL = "http://localhost:8081/v1/keys";
    private static final String DICT_DELETE_URL = "http://localhost:8081/v1/keys/11660117046";

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public String cretePixKey(final String keyType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestIdentifier", UUID.randomUUID().toString());
        headers.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(PixKeyJsonHelper.createCPF(), headers);

        var result = restTemplate.postForObject(DICT_CREATE_URL, request, String.class);
        return mapper.readTree(result).get("key").textValue();
    }

    public void clearAllDataForTheTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestIdentifier", UUID.randomUUID().toString());
        headers.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(PixKeyJsonHelper.deleteCPF(), headers);

        restTemplate.exchange(DICT_DELETE_URL, HttpMethod.DELETE, request, String.class);
    }

}
