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
        String s = "<CreateEntryResponse>\n" +
                "    <Signature></Signature>\n" +
                "    <Entry>\n" +
                "        <Key>11122233300</Key>\n" +
                "        <KeyType>CPF</KeyType>\n" +
                "        <Account>\n" +
                "            <Participant>12345678</Participant>\n" +
                "            <Branch>0001</Branch>\n" +
                "            <AccountNumber>0007654321</AccountNumber>\n" +
                "            <AccountType>CACC</AccountType>\n" +
                "            <OpeningDate>2010-01-10T03:00:00Z</OpeningDate>\n" +
                "        </Account>\n" +
                "        <Owner>\n" +
                "            <Type>NATURAL_PERSON</Type>\n" +
                "            <TaxIdNumber>11122233300</TaxIdNumber>\n" +
                "            <Name>João Silva</Name>\n" +
                "        </Owner>\n" +
                "        <CreationDate>2019-11-18T03:00:00Z</CreationDate>\n" +
                "        <KeyOwnershipDate>2019-11-18T03:00:00Z</KeyOwnershipDate>\n" +
                "    </Entry>\n" +
                "</CreateEntryResponse>";
        return HttpResponse.response()
                .withContentType(MediaType.APPLICATION_XML)
                .withStatusCode(HttpStatusCode.CREATED_201.code())
                .withBody(s);
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
