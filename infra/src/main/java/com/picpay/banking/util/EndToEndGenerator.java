package com.picpay.banking.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class EndToEndGenerator {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public static String generate(final String ispb) {
        var uuid = UUID.randomUUID().toString();
        var sequential = uuid.substring(uuid.length() - 11, uuid.length());

        return new StringBuilder("E")
                .append(ispb)
                .append(DATE_TIME_FORMATTER.format(LocalDateTime.now(ZoneId.of("UTC"))))
                .append(sequential)
                .toString();
    }

}
