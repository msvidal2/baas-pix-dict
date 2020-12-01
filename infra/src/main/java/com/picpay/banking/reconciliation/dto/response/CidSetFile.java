package com.picpay.banking.reconciliation.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile.StatusContentIdentifierFile;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CidSetFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class CidSetFile {

    @XmlElement(name = "Id")
    private Integer id;

    @XmlElement(name = "Status")
    private StatusContentIdentifierFile status;

    @XmlElement(name = "Participant")
    private Integer participant;

    @XmlElement(name = "KeyType")
    private KeyTypeBacen keyType;

    @XmlElement(name = "RequestTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime requestTime;


    @XmlElement(name = "CreationTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime creationTime;

    @XmlElement(name = "Url")
    private String url;

    @XmlElement(name = "Bytes")
    private Long length;

    @XmlElement(name = "Sha256")
    private String sha256;

    public ContentIdentifierFile toDomain() {
        return ContentIdentifierFile.builder()
            .id(id)
            .requestTime(requestTime)
            .status(status)
            .keyType(keyType.getType())
            .sha256(sha256)
            .url(url)
            .length(length)
        .build();
    }

}
