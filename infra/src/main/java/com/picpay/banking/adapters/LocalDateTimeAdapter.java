package com.picpay.banking.adapters;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private final static String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private final DateTimeFormatter formatter;

    public LocalDateTimeAdapter() {
        formatter = DateTimeFormatter.ofPattern(PATTERN);
    }

    @Override
    public LocalDateTime unmarshal(String value) throws Exception {
        log.info("unmarshal: {}", value);

        if(Strings.isNullOrEmpty(value) || value.isBlank()) {
            return null;
        }
        
        return LocalDateTime.from(formatter.parse(value));
    }

    @Override
    public String marshal(LocalDateTime value) throws Exception {
        log.info("marshal: {}", value);

        if(value == null) {
            return null;
        }

        return formatter.format(value);
    }

}
