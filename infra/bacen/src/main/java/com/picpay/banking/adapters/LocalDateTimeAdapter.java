package com.picpay.banking.adapters;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    public LocalDateTime unmarshal(String value) {
        if(Strings.isNullOrEmpty(value) || value.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.from(formatter.parse(value));
        }catch (DateTimeParseException e){
            return LocalDateTime.from(formatterNano.parse(value));
        }
    }

    @Override
    public String marshal(LocalDateTime value)  {
        if(value == null) {
            return null;
        }

        try {
            return formatter.format(value);
        }catch (DateTimeParseException  e){
            return formatterNano.format(value);
        }
    }

}
