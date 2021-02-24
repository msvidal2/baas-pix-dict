package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static com.picpay.banking.pix.core.domain.ContentIdentifierFile.StatusContentIdentifierFile.DONE;

/**
 * @author Luis Silva
 * @version 1.0 25/11/2020
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
public class ContentIdentifierFile {

    private StatusContentIdentifierFile status;
    private final Integer id;
    private final KeyType keyType;
    private final LocalDateTime requestTime;

    private final String url;
    private final Long length;
    private final String sha256;
    private List<String> content;

    public void done(){
        this.status = DONE;
    }

    public void addContent(List<String> content) {
        this.content = content;
    }

    public boolean isNotProcessed() {
        return status != DONE;
    }

    public enum StatusContentIdentifierFile{
        REQUESTED,
        AVAILABLE,
        PROCESSING,
        DONE;

        public boolean isNotAvailable() {
            return this != AVAILABLE;
        }
    }

}
