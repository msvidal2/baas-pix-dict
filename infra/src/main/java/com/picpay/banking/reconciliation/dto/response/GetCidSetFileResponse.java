package com.picpay.banking.reconciliation.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
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
@XmlRootElement(name = "GetCidSetFileResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetCidSetFileResponse {

    @XmlElement(name = "ResponseTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    @XmlElement(name = "CidSetFile")
    private CidSetFile cidSetFile;

    public ContentIdentifierFile toDomain() {
        return cidSetFile.toDomain();
    }

}
