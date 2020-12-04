package com.picpay.banking.reconciliation.dto.response;

import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CreateSyncVerificationResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateSyncVerificationResponse {

    @XmlElement(name = "SyncVerification")
    private SyncVerification syncVerification;

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

        @XmlElement(name = "Id")
        private Integer id;

        @XmlElement(name = "Result")
        private Result result;

    }

}
