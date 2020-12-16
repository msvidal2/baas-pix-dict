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

    private static final String DICT_BASE_URL = "http://localhost:8081/v1/keys";
    private static final String CPF = "11660117046";

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final ObjectMapper mapper = new ObjectMapper();

    public void clearAllDataForTheTest() {
        // TODO: Falta implementar a chamada para os outros tipos de chaves
        try {
            restTemplate.exchange(DICT_BASE_URL + "/" + CPF, HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.deleteCPF()), String.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getResponseBodyAsString());
        }
    }

    @SneakyThrows
    public String cretePixKey(final String keyType) {
        var result = restTemplate.postForObject(DICT_BASE_URL, createHttpEntity(PixKeyJsonHelper.createCPF()), String.class);
        return mapper.readTree(result).get("key").textValue();
    }

    @SneakyThrows
    public String updatePixKey(final String keyType) {
        var result = restTemplate.exchange(DICT_BASE_URL + "/" + CPF, HttpMethod.PUT, createHttpEntity(PixKeyJsonHelper.updateCPF()), String.class);
        return mapper.readTree(result.getBody()).get("key").textValue();
    }

    public String removePixKey(final String keyType) {
        restTemplate.exchange(DICT_BASE_URL + "/" + CPF, HttpMethod.DELETE, createHttpEntity(PixKeyJsonHelper.deleteCPF()), String.class);
        return "11660117046";
    }


    private HttpEntity<String> createHttpEntity(String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestIdentifier", UUID.randomUUID().toString());
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(jsonBody, headers);
    }

}
