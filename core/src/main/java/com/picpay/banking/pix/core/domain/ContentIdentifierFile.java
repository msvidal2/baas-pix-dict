package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author Luis Silva
 * @version 1.0 25/11/2020
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
public class ContentIdentifierFile {

    private Integer id;
    private StatusContentIdentifierFile status;
    private KeyType keyType;
    private LocalDateTime requestTime;

    private String url;
    private Long length;
    private String sha256;

    public enum StatusContentIdentifierFile{
        REQUESTED,
        AVAILABLE,
        PROCESSING;

        public boolean isNotAvaliable() {
            return !this.equals(AVAILABLE);
        }
    }

}
