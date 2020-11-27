/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.mockserver.dict.infraction;

import com.picpay.banking.mockserver.config.ClientAndServerInstance;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.mockserver.model.HttpRequest.request;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@Slf4j
public class InfractionCrudBacenServer {

    @PostConstruct
    public void start() {
        ClientAndServerInstance.get().when(
            request()
                .withMethod("POST")
                .withPath("/api/v1/infraction-reports"))
            .respond(this::createInfractionReport);
    }

    private HttpResponse createInfractionReport(final HttpRequest httpRequest) {
        log.info(httpRequest.getBodyAsJsonOrXmlString());
        String xmlResponse = "<CreateInfractionReportResponse>\n" +
            "    <InfractionReport>\n" +
            "        <TransactionId>E9999901012341234123412345678900</TransactionId>\n" +
            "        <InfractionType>FRAUD</InfractionType>\n" +
            "        <ReportedBy>DEBITED_PARTICIPANT</ReportedBy>\n" +
            "        <ReportDetails>Transação feita através de QR Code falso em boleto</ReportDetails>\n" +
            "        <Id>91d65e98-97c0-4b0f-b577-73625da1f9fc</Id>\n" +
            "        <Status>OPEN</Status>\n" +
            "        <DebitedParticipant>1234</DebitedParticipant>\n" +
            "        <CreditedParticipant>56789</CreditedParticipant>\n" +
            "        <CreationTime>2020-01-17T10:00:00Z</CreationTime>\n" +
            "        <LastModified>2020-01-17T10:00:00Z</LastModified>\n" +
            "    </InfractionReport>\n" +
            "</CreateInfractionReportResponse>";
        return HttpResponse.response()
            .withContentType(MediaType.APPLICATION_XML)
            .withStatusCode(HttpStatusCode.CREATED_201.code())
            .withBody(xmlResponse);
    }

}
