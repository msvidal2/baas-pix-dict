package com.picpay.banking.mockserver.dict.pixkey;

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
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 17/11/20
 */
@Slf4j
@Component
public class PixKeyCrudBacenServer {

    @PostConstruct
    public void start() {
        ClientAndServerInstance.get().when(
                request()
                        .withMethod("POST")
                        .withPath("/v1/entries"))
                .respond(this::createPixKey);

        ClientAndServerInstance.get().when(
                request()
                        .withMethod("PUT")
                        .withPath("/v1/entries"))
                .respond(this::updatePixKey);

        ClientAndServerInstance.get().when(
                request()
                        .withMethod("GET")
                        .withPath("/v1/entries/{key}"))
                .respond(this::findPixKey);

        ClientAndServerInstance.get().when(
                request()
                        .withMethod("POST")
                        .withPath("/v1/entries/{key}/delete"))
                .respond(this::removePixKey);
    }

    private HttpResponse createPixKey(final HttpRequest httpRequest) {
        log.info(httpRequest.getBodyAsJsonOrXmlString());
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
                .append("<CreateEntryResponse>\n")
                .append("    <Signature></Signature>\n")
                .append("    <ResponseTime>2020-01-10T10:00:00Z</ResponseTime>\n")
                .append("    <CorrelationId>a9f13566e19f5ca51329479a5bae60c5</CorrelationId>\n")
                .append("    <Entry>\n")
                .append("        <Key>11122233300</Key>\n")
                .append("        <KeyType>CPF</KeyType>\n")
                .append("        <Account>\n")
                .append("            <Participant>12345678</Participant>\n")
                .append("            <Branch>0001</Branch>\n")
                .append("            <AccountNumber>0007654321</AccountNumber>\n")
                .append("            <AccountType>CACC</AccountType>\n")
                .append("            <OpeningDate>2010-01-10T03:00:00Z</OpeningDate>\n")
                .append("        </Account>\n")
                .append("        <Owner>\n")
                .append("            <Type>NATURAL_PERSON</Type>\n")
                .append("            <TaxIdNumber>11122233300</TaxIdNumber>\n")
                .append("            <Name>João Silva</Name>\n")
                .append("        </Owner>\n")
                .append("        <CreationDate>2019-11-18T03:00:00Z</CreationDate>\n")
                .append("        <KeyOwnershipDate>2019-11-18T03:00:00Z</KeyOwnershipDate>\n")
                .append("    </Entry>\n")
                .append("</CreateEntryResponse>");
        return HttpResponse.response()
                .withContentType(MediaType.APPLICATION_XML_UTF_8)
                .withStatusCode(HttpStatusCode.CREATED_201.code())
                .withBody(sb.toString());
    }

    private HttpResponse updatePixKey(final HttpRequest httpRequest) {
        return null;
    }

    private HttpResponse findPixKey(final HttpRequest httpRequest) {
        return null;
    }

    private HttpResponse removePixKey(final HttpRequest httpRequest) {
        return null;
    }

}
