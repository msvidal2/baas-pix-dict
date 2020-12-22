package com.picpay.banking.pix.dict.test.dictapi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PixKeyJsonHelper {

    public static String createCPF() {
        return "{\n" +
            "    \"type\": \"CPF\",\n" +
            "    \"key\": \"11660117046\",\n" +
            "    \"ispb\": 22896431,\n" +
            "    \"branchNumber\": \"0001\",\n" +
            "    \"accountType\": \"CHECKING\",\n" +
            "    \"accountNumber\": \"0001098099\",\n" +
            "    \"accountOpeningDate\": \"2020-12-12T12:12:12.000\",\n" +
            "    \"personType\": \"INDIVIDUAL_PERSON\",\n" +
            "    \"cpfCnpj\": \"11660117046\",\n" +
            "    \"name\": \"Rodrigo Argentato\",\n" +
            "    \"reason\": \"CLIENT_REQUEST\"\n" +
            "}";
    }

    public static String updateCPF() {
        return "{\n" +
            "  \"accountNumber\": \"0001098200\",\n" +
            "  \"accountOpeningDate\": \"2020-08-14T13:59:12.000Z\",\n" +
            "  \"accountType\": \"CHECKING\",\n" +
            "  \"branchNumber\": \"0004\",\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"CPF\",\n" +
            "  \"userId\": \"93405736099\"\n" +
            "}";
    }

    public static String deleteCPF() {
        return "{\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"CPF\"\n" +
            "}";
    }

}
