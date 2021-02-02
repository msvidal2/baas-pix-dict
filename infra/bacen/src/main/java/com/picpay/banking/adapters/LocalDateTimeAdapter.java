package com.picpay.banking.adapters;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String PATTERN_WITH_NANOSECOND = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final DateTimeFormatter formatter;
    private final DateTimeFormatter formatterNano;

    public LocalDateTimeAdapter() {
        formatter = DateTimeFormatter.ofPattern(PATTERN);
        formatterNano = DateTimeFormatter.ofPattern(PATTERN_WITH_NANOSECOND);
    }

    @Override
    public LocalDateTime unmarshal(String value) throws Exception {
        if(Strings.isNullOrEmpty(value) || value.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.from(formatter.parse(value));
        }catch (Exception e){
            log.trace("Invalid Format {} - {}", PATTERN, value);
            return LocalDateTime.from(formatterNano.parse(value));
        }
    }

    @Override
    public String marshal(LocalDateTime value) throws Exception {
        if(value == null) {
            return null;
        }

        try {
            return formatter.format(value);
        }catch (Exception e){
            log.trace("Invalid Format {} - {}", PATTERN, value);
            return formatterNano.format(value);
        }
    }

}
