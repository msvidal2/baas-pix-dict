package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.ContentIdentifierFile.StatusContentIdentifierFile.*;

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

    public void done(){
        this.status = DONE;
    }

    public boolean isNotProcessed() {
        return status != DONE;
    }

    public enum StatusContentIdentifierFile{
        REQUESTED,
        AVAILABLE,
        PROCESSING,
        DONE;

        public boolean isNotAvaliable() {
            return this != AVAILABLE;
        }
    }

}
