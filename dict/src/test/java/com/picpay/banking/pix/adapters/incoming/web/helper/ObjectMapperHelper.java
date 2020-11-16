package com.picpay.banking.pix.adapters.incoming.web.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public enum ObjectMapperHelper {

    OBJECT_MAPPER;

    private ObjectMapper objectMapper;

    ObjectMapperHelper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String asJsonString(Object object) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}
