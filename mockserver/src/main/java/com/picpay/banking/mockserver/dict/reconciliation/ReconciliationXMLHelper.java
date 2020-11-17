package com.picpay.banking.mockserver.dict.reconciliation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReconciliationXMLHelper {

    public static byte[] createSyncVerificationResponse(String participant, String keyType, String participantSyncVerifier, String result) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<CreateSyncVerificationResponse>\n" +
            "    <Signature></Signature>\n" +
            "    <ResponseTime>%s</ResponseTime>\n" +
            "    <CorrelationId>%s</CorrelationId>\n" +
            "    <SyncVerification>\n" +
            "        <Participant>%s</Participant>\n" +
            "        <KeyType>%s</KeyType>\n" +
            "        <ParticipantSyncVerifier>%s</ParticipantSyncVerifier>\n" +
            "        <Id>%s</Id>\n" +
            "        <Result>%s</Result>\n" +
            "    </SyncVerification>\n" +
            "</CreateSyncVerificationResponse>";

        final String responseTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        final String correlationId = UUID.randomUUID().toString();
        final String id = String.valueOf(new Random().nextInt());

        return String.format(xml,
            responseTime, correlationId, participant, keyType, participantSyncVerifier, id, result).getBytes();
    }

}
