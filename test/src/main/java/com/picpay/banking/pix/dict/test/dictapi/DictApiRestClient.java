package com.picpay.banking.pix.dict.test.dictapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
public class DictApiRestClient {

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${picpay.dict.url}")
    private String dictUrl;

    public void clearAllDataForTheTest() {
        try {
            restTemplate.exchange(dictUrl + "/" + PixKeyJsonHelper.CPF,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("CPF")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
        try {
            restTemplate.exchange(dictUrl + "/" + PixKeyJsonHelper.CNPJ,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("CNPJ")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
        try {
            restTemplate.exchange(dictUrl + "/" + PixKeyJsonHelper.CELLPHONE,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("CELLPHONE")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
        try {
            restTemplate.exchange(dictUrl + "/" + PixKeyJsonHelper.EMAIL,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("EMAIL")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
    }

    @SneakyThrows
    public String cretePixKey(final String keyType) {
        var result = restTemplate.postForObject(dictUrl, createHttpEntity(PixKeyJsonHelper.createBody(keyType)), String.class);
        var keyValue = mapper.readTree(result).get("key").textValue();
        if ("RANDOM".equals(keyType)) PixKeyJsonHelper.RANDOM = keyValue;
        return keyValue;
    }

    @SneakyThrows
    public String updatePixKey(final String keyType) {
        var result = restTemplate.exchange(dictUrl + "/" + PixKeyJsonHelper.keyByKeyType(keyType),
            HttpMethod.PUT, createHttpEntity(PixKeyJsonHelper.updateBody(keyType)), String.class);
        return mapper.readTree(result.getBody()).get("key").textValue();
    }

    public String removePixKey(final String keyType) {
        restTemplate.exchange(dictUrl + "/" + PixKeyJsonHelper.keyByKeyType(keyType),
            HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType(keyType)), String.class);
        return PixKeyJsonHelper.keyByKeyType(keyType);
    }


    private HttpEntity<String> createHttpEntity(String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestIdentifier", UUID.randomUUID().toString());
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(jsonBody, headers);
    }

}
