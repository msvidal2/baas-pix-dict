package com.picpay.banking.fallbacks;

import java.util.Map;

public class DefaultFieldResolver implements FieldResolver {

    @Override
    public Map<String, String> fieldsMap() {
        return Map.of();
    }

}
