package com.picpay.banking.pix.dict.test.dictapi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PixKeyJsonHelper {

    public static final String CPF = "11660117046";
    public static final String CNPJ = "30929187000173";
    public static final String CELLPHONE = "+5516920202021";
    public static final String EMAIL = "rodrigopizzi@gmail.com";
    public static String RANDOM = "";

    private static final String ACCOUNT_OPENING_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").format(OffsetDateTime.now());
    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("keyType is invalid");

    public static String createBody(String keyType) {
        switch (keyType) {
            case "CPF":
                return createCPF();
            case "CNPJ":
                return createCNPJ();
            case "CELLPHONE":
                return createCellphone();
            case "EMAIL":
                return createEmail();
            case "RANDOM":
                return createRandom();
            default:
                throw ILLEGAL_ARGUMENT_EXCEPTION;
        }
    }

    public static String updateBody(String keyType) {
        switch (keyType) {
            case "CPF":
                return updateCPF();
            case "CNPJ":
                return updateCNPJ();
            case "CELLPHONE":
                return updateCellphone();
            case "EMAIL":
                return updateEmail();
            case "RANDOM":
                return updateRandom();
            default:
                throw ILLEGAL_ARGUMENT_EXCEPTION;
        }
    }

    public static String keyByKeyType(String keyType) {
        switch (keyType) {
            case "CPF":
                return CPF;
            case "CNPJ":
                return CNPJ;
            case "CELLPHONE":
                return CELLPHONE;
            case "EMAIL":
                return EMAIL;
            case "RANDOM":
                return RANDOM;
            default:
                throw ILLEGAL_ARGUMENT_EXCEPTION;
        }
    }

    private static String createCPF() {
        return String.format("{\n" +
            "    \"type\": \"CPF\",\n" +
            "    \"key\": \"11660117046\",\n" +
            "    \"ispb\": 22896431,\n" +
            "    \"branchNumber\": \"0001\",\n" +
            "    \"accountType\": \"CHECKING\",\n" +
            "    \"accountNumber\": \"0001098099\",\n" +
            "    \"accountOpeningDate\": \"%s\",\n" +
            "    \"personType\": \"INDIVIDUAL_PERSON\",\n" +
            "    \"cpfCnpj\": \"11660117046\",\n" +
            "    \"name\": \"Rodrigo Argentato\",\n" +
            "    \"reason\": \"CLIENT_REQUEST\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    private static String updateCPF() {
        return String.format("{\n" +
            "  \"accountNumber\": \"0001098200\",\n" +
            "  \"accountOpeningDate\": \"%s\",\n" +
            "  \"accountType\": \"CHECKING\",\n" +
            "  \"branchNumber\": \"0004\",\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"CPF\",\n" +
            "  \"userId\": \"93405736099\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    private static String createCNPJ() {
        return String.format("{\n" +
            "    \"type\": \"CNPJ\",\n" +
            "    \"key\": \"30929187000173\",\n" +
            "    \"ispb\": 22896431,\n" +
            "    \"branchNumber\": \"0001\",\n" +
            "    \"accountType\": \"CHECKING\",\n" +
            "    \"accountNumber\": \"0002098099\",\n" +
            "    \"accountOpeningDate\": \"%s\",\n" +
            "    \"personType\": \"LEGAL_ENTITY\",\n" +
            "    \"cpfCnpj\": \"30929187000173\",\n" +
            "    \"fantasyName\": \"Mala Pronta SA\",\n" +
            "    \"name\": \"Mala Pronta SA\",\n" +
            "    \"reason\": \"CLIENT_REQUEST\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    private static String updateCNPJ() {
        return String.format("{\n" +
            "  \"accountNumber\": \"0002098100\",\n" +
            "  \"accountOpeningDate\": \"%s\",\n" +
            "  \"accountType\": \"CHECKING\",\n" +
            "  \"branchNumber\": \"0004\",\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"CNPJ\",\n" +
            "  \"userId\": \"93405736099\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    private static String createCellphone() {
        return String.format("{\n" +
            "    \"type\": \"CELLPHONE\",\n" +
            "    \"key\": \"+5516920202021\",\n" +
            "    \"ispb\": 22896431,\n" +
            "    \"branchNumber\": \"0001\",\n" +
            "    \"accountType\": \"CHECKING\",\n" +
            "    \"accountNumber\": \"0003098099\",\n" +
            "    \"accountOpeningDate\": \"%s\",\n" +
            "    \"personType\": \"INDIVIDUAL_PERSON\",\n" +
            "    \"cpfCnpj\": \"91190908034\",\n" +
            "    \"name\": \"Rodrigo Argentato\",\n" +
            "    \"reason\": \"CLIENT_REQUEST\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    public static String updateCellphone() {
        return String.format("{\n" +
            "  \"accountNumber\": \"0003098100\",\n" +
            "  \"accountOpeningDate\": \"%s\",\n" +
            "  \"accountType\": \"CHECKING\",\n" +
            "  \"branchNumber\": \"0004\",\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"CELLPHONE\",\n" +
            "  \"userId\": \"93405736099\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    private static String createEmail() {
        return String.format("{\n" +
            "    \"type\": \"EMAIL\",\n" +
            "    \"key\": \"rodrigopizzi@gmail.com\",\n" +
            "    \"ispb\": 22896431,\n" +
            "    \"branchNumber\": \"0001\",\n" +
            "    \"accountType\": \"CHECKING\",\n" +
            "    \"accountNumber\": \"0004098099\",\n" +
            "    \"accountOpeningDate\": \"%s\",\n" +
            "    \"personType\": \"INDIVIDUAL_PERSON\",\n" +
            "    \"cpfCnpj\": \"58486168090\",\n" +
            "    \"name\": \"Rodrigo Argentato\",\n" +
            "    \"reason\": \"CLIENT_REQUEST\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    public static String updateEmail() {
        return String.format("{\n" +
            "  \"accountNumber\": \"0004098100\",\n" +
            "  \"accountOpeningDate\": \"%s\",\n" +
            "  \"accountType\": \"CHECKING\",\n" +
            "  \"branchNumber\": \"0004\",\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"EMAIL\",\n" +
            "  \"userId\": \"93405736099\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    private static String createRandom() {
        return String.format("{\n" +
            "    \"type\": \"RANDOM\",\n" +
            "    \"ispb\": 22896431,\n" +
            "    \"branchNumber\": \"0001\",\n" +
            "    \"accountType\": \"CHECKING\",\n" +
            "    \"accountNumber\": \"0005098099\",\n" +
            "    \"accountOpeningDate\": \"%s\",\n" +
            "    \"personType\": \"INDIVIDUAL_PERSON\",\n" +
            "    \"cpfCnpj\": \"70631471022\",\n" +
            "    \"name\": \"Rodrigo Argentato\",\n" +
            "    \"reason\": \"CLIENT_REQUEST\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    public static String updateRandom() {
        return String.format("{\n" +
            "  \"accountNumber\": \"0005098100\",\n" +
            "  \"accountOpeningDate\": \"%s\",\n" +
            "  \"accountType\": \"CHECKING\",\n" +
            "  \"branchNumber\": \"0004\",\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"RANDOM\",\n" +
            "  \"userId\": \"93405736099\"\n" +
            "}", ACCOUNT_OPENING_DATE);
    }

    public static String removeByType(String keyType) {
        return String.format("{\n" +
            "  \"ispb\": 22896431,\n" +
            "  \"reason\": \"CLIENT_REQUEST\",\n" +
            "  \"type\": \"%s\"\n" +
            "}", keyType);
    }

}
