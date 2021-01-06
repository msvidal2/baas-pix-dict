package com.picpay.banking.pix.dict.test.dictapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

    private static final String DICT_BASE_URL = "http://localhost:8080/v1/keys";

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final ObjectMapper mapper = new ObjectMapper();

    public void clearAllDataForTheTest() {
        try {
            restTemplate.exchange(DICT_BASE_URL + "/" + PixKeyJsonHelper.CPF,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("CPF")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
        try {
            restTemplate.exchange(DICT_BASE_URL + "/" + PixKeyJsonHelper.CNPJ,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("CNPJ")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
        try {
            restTemplate.exchange(DICT_BASE_URL + "/" + PixKeyJsonHelper.CELLPHONE,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("CELLPHONE")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
        try {
            restTemplate.exchange(DICT_BASE_URL + "/" + PixKeyJsonHelper.EMAIL,
                HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.removeByType("EMAIL")), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
    }

    @SneakyThrows
    public String cretePixKey(final String keyType) {
        var result = restTemplate.postForObject(DICT_BASE_URL, createHttpEntity(PixKeyJsonHelper.createBody(keyType)), String.class);
        var keyValue = mapper.readTree(result).get("key").textValue();
        if ("RANDOM".equals(keyType)) PixKeyJsonHelper.RANDOM = keyValue;
        return keyValue;
    }

    @SneakyThrows
    public String updatePixKey(final String keyType) {
        var result = restTemplate.exchange(DICT_BASE_URL + "/" + PixKeyJsonHelper.keyByKeyType(keyType),
            HttpMethod.PUT, createHttpEntity(PixKeyJsonHelper.updateBody(keyType)), String.class);
        return mapper.readTree(result.getBody()).get("key").textValue();
    }

    public String removePixKey(final String keyType) {
        restTemplate.exchange(DICT_BASE_URL + "/" + PixKeyJsonHelper.keyByKeyType(keyType),
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
