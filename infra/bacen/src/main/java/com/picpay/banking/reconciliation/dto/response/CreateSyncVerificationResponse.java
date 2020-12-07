package com.picpay.banking.reconciliation.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.adapters.ZoneDateTimeAdapter;
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
import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CreateSyncVerificationResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateSyncVerificationResponse {

    @XmlElement(name = "SyncVerification")
    private SyncVerification syncVerification;

    @XmlElement(name = "ResponseTime")
    @XmlJavaTypeAdapter(ZoneDateTimeAdapter.class)
    private ZonedDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    public enum Result {
        OK,
        NOK
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "SyncVerification")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SyncVerification {

        @XmlElement(name = "Participant")
        private String participant;

        @XmlElement(name = "KeyType")
        private KeyTypeBacen keyType;

        @XmlElement(name = "ParticipantSyncVerifier")
        private String participantSyncVerifier;

        @XmlElement(name = "SyncVerifierLastModified")
        @XmlJavaTypeAdapter(ZoneDateTimeAdapter.class)
        private ZonedDateTime syncVerifierLastModified;

        @XmlElement(name = "Id")
        private Integer id;

        @XmlElement(name = "Result")
        private Result result;

    }

}
