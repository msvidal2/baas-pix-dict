package com.picpay.banking.reconciliation.dto.request;

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
@XmlRootElement(name = "CreateSyncVerificationRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateSyncVerificationRequest {

    @XmlElement(name = "SyncVerification")
    private SyncVerification syncVerification;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "SyncVerification")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SyncVerification {

        @XmlElement(name = "Participant")
        private Integer participant;

        @XmlElement(name = "KeyType")
        private KeyTypeBacen keyType;

        @XmlElement(name = "ParticipantSyncVerifier")
        private String participantSyncVerifier;

    }

}
