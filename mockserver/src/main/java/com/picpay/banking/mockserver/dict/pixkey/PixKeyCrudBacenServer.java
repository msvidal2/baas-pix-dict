package com.picpay.banking.mockserver.dict.pixkey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.MediaType;
import org.mockserver.model.Parameter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.mockserver.model.HttpRequest.request;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 17/11/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PixKeyCrudBacenServer {

    private final ClientAndServer clientAndServer;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @PostConstruct
    public void start() {
        clientAndServer.when(
            request()
                .withMethod("POST")
                .withPath("/v1/entries"))
            .respond(this::createPixKey);

        clientAndServer.when(
            request()
                .withMethod("PUT")
                .withPath("/v1/entries"))
            .respond(this::updatePixKey);

        clientAndServer.when(
            request()
                .withMethod("GET")
                .withPath("/v1/entries/{key}")
                .withPathParameters(
                    Parameter.param("key", "[\\S]*")
                                   ))
            .respond(this::findPixKey);

        clientAndServer.when(
            request()
                .withMethod("POST")
                .withPath("/v1/entries/{key}/delete"))
            .respond(this::removePixKey);
    }

    private HttpResponse createPixKey(final HttpRequest httpRequest) throws JsonProcessingException {
        XmlMapper mapper = new XmlMapper();
        JsonNode jsonNode = mapper.readTree(httpRequest.getBodyAsJsonOrXmlString());
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
            .append("<CreateEntryResponse>\n")
            .append("    <Signature></Signature>\n")
            .append("    <ResponseTime>").append(formatter.format(LocalDateTime.now(ZoneId.of("UTC")))).append("</ResponseTime>\n")
            .append("    <CorrelationId>").append(generateRandomCorrelationId()).append("</CorrelationId>\n")
            .append("    <Entry>\n")
            .append("        <Key>").append(jsonNode.get("Entry").get("Key").asText()).append("</Key>\n")
            .append("        <KeyType>").append(jsonNode.get("Entry").get("KeyType").asText()).append("</KeyType>\n")
            .append("        <Account>\n")
            .append("            <Participant>").append(jsonNode.get("Entry").get("Account").get("Participant").asText()).append("</Participant>\n")
            .append("            <Branch>").append(jsonNode.get("Entry").get("Account").get("Branch").asText()).append("</Branch>\n")
            .append("            <AccountNumber>").append(jsonNode.get("Entry").get("Account").get("AccountNumber").asText()).append(
            "</AccountNumber>\n")
            .append("            <AccountType>").append(jsonNode.get("Entry").get("Account").get("AccountType").asText()).append("</AccountType>\n")
            .append("            <OpeningDate>").append(jsonNode.get("Entry").get("Account").get("OpeningDate").asText()).append("</OpeningDate>\n")
            .append("        </Account>\n")
            .append("        <Owner>\n")
            .append("            <Type>").append(jsonNode.get("Entry").get("Owner").get("Type").asText()).append("</Type>\n")
            .append("            <TaxIdNumber>").append(jsonNode.get("Entry").get("Owner").get("TaxIdNumber").asText()).append("</TaxIdNumber>\n")
            .append("            <Name>").append(jsonNode.get("Entry").get("Owner").get("Name").asText()).append("</Name>\n")
            .append("        </Owner>\n")
            .append("        <CreationDate>").append(formatter.format(LocalDateTime.now(ZoneId.of("UTC")))).append("</CreationDate>\n")
            .append("        <KeyOwnershipDate>").append(formatter.format(LocalDateTime.now(ZoneId.of("UTC")))).append("</KeyOwnershipDate>\n")
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
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
            .append("<GetEntryResponse>\n")
            .append("   <Signature></Signature>\n")
            .append("   <ResponseTime>2020-01-10T10:00:00Z</ResponseTime>\n")
            .append("   <CorrelationId>a9f13566e19f5ca51329479a5bae60c5</CorrelationId>\n")
            .append("   <Entry>\n")
            .append("       <Key>11122233300</Key>\n")
            .append("       <KeyType>CPF</KeyType>\n")
            .append("       <Account>\n")
            .append("           <Participant>12345678</Participant>\n")
            .append("           <Branch>0001</Branch>\n")
            .append("           <AccountNumber>0007654321</AccountNumber>\n")
            .append("           <AccountType>CACC</AccountType>\n")
            .append("           <OpeningDate>2010-01-10T03:00:00Z</OpeningDate>\n")
            .append("       </Account>\n")
            .append("       <Owner>\n")
            .append("           <Type>NATURAL_PERSON</Type>\n")
            .append("           <TaxIdNumber>11122233300</TaxIdNumber>\n")
            .append("           <Name>João Silva</Name>\n")
            .append("       </Owner>\n")
            .append("       <CreationDate>2019-11-18T03:00:00Z</CreationDate>\n")
            .append("       <KeyOwnershipDate>2019-11-18T03:00:00Z</KeyOwnershipDate>\n")
            .append("       <OpenClaimCreationDate>2019-11-19T03:00:00Z</OpenClaimCreationDate>\n")
            .append("   </Entry>\n")
            .append("   <Statistics>\n")
            .append("       <LastUpdated>2020-01-17T10:00:00Z</LastUpdated>\n")
            .append("       <Counters>\n")
            .append("           <Counter type=\"SETTLEMENTS\" by=\"KEY\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"SETTLEMENTS\" by=\"OWNER\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"SETTLEMENTS\" by=\"ACCOUNT\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"REPORTED_FRAUDS\" by=\"KEY\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"REPORTED_FRAUDS\" by=\"OWNER\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"REPORTED_FRAUDS\" by=\"ACCOUNT\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"REPORTED_AML_CFT\" by=\"KEY\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"REPORTED_AML_CFT\" by=\"OWNER\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"REPORTED_AML_CFT\" by=\"ACCOUNT\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"CONFIRMED_FRAUDS\" by=\"KEY\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"CONFIRMED_FRAUDS\" by=\"OWNER\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"CONFIRMED_FRAUDS\" by=\"ACCOUNT\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"CONFIRMED_AML_CFT\" by=\"KEY\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"CONFIRMED_AML_CFT\" by=\"OWNER\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("           <Counter type=\"CONFIRMED_AML_CFT\" by=\"ACCOUNT\" d3=\"0\" d30=\"5\" m6=\"30\"/>\n")
            .append("       </Counters>\n")
            .append("   </Statistics>\n")
            .append("</GetEntryResponse>\n");
        return HttpResponse.response()
            .withContentType(MediaType.APPLICATION_XML_UTF_8)
            .withStatusCode(HttpStatusCode.OK_200.code())
            .withBody(sb.toString());
    }

    private HttpResponse removePixKey(final HttpRequest httpRequest) throws JsonProcessingException {
        XmlMapper mapper = new XmlMapper();
        JsonNode jsonNode = mapper.readTree(httpRequest.getBodyAsJsonOrXmlString());
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
            .append("<DeleteEntryResponse>\n")
            .append("    <Signature></Signature>\n")
            .append("    <ResponseTime>").append(formatter.format(LocalDateTime.now(ZoneId.of("UTC")))).append("</ResponseTime>\n")
            .append("    <CorrelationId>").append(generateRandomCorrelationId()).append("</CorrelationId>\n")
            .append("    <Key>+5561988887777</Key>\n")
            .append("</DeleteEntryResponse>");
        return HttpResponse.response()
            .withContentType(MediaType.APPLICATION_XML_UTF_8)
            .withStatusCode(HttpStatusCode.OK_200.code())
            .withBody(sb.toString());
    }

    private String generateRandomCorrelationId() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            if (random.nextBoolean()) {
                int randomLimitedInt = leftLimit + (random.nextInt() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            } else {
                buffer.append(random.nextInt(9));
            }
        }
        return buffer.toString();
    }

}
