package com.picpay.banking.adapters;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ZoneDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

    private final static String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private final static String PATTERN_WITH_NANOSECOND = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final DateTimeFormatter formatter;
    private final DateTimeFormatter formatterNano;

    public ZoneDateTimeAdapter() {
        formatter = DateTimeFormatter.ofPattern(PATTERN).withZone(ZoneId.of("UTC"));
        formatterNano = DateTimeFormatter.ofPattern(PATTERN_WITH_NANOSECOND).withZone(ZoneId.of("UTC"));
    }

    @Override
    public ZonedDateTime unmarshal(String value) throws Exception {
        if (Strings.isNullOrEmpty(value) || value.isBlank()) {
            return null;
        }
        try {
            return ZonedDateTime.from(formatter.parse(value));
        } catch (Exception e) {
            return ZonedDateTime.from(formatterNano.parse(value));
        }
    }

    @Override
    public String marshal(ZonedDateTime value) throws Exception {
        if (value == null) {
            return null;
        }

        try {
            return formatter.format(value);
        } catch (Exception e) {
            return formatterNano.format(value);
        }
    }

}
