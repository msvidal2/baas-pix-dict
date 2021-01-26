package com.picpay.banking.pix.core.domain;


import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PixKeyTest {

    @Test
    void calculateCidByCPF() {
        var pixKey = PixKey.builder()
            .ispb(22896431)
            .key("35618656825")
            .type(KeyType.CPF)
            .branchNumber("1")
            .accountNumber("3945812")
            .accountType(AccountType.CHECKING)
            .taxId("35618656825")
            .name("Rafael Aramizu Gomes")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .requestId(UUID.fromString("728b45cc-7f8f-43a6-b921-b2f06c85dcfc"))
            .build();

        pixKey.calculateCid();

        final String expectedCid = "85de03695a98e4ee0434bc40255e375e7fd0849f2a2a2143f58d31fd04fd2da4";
        assertThat(pixKey.getCid()).isEqualTo(expectedCid);
    }

    @Test
    void calculateCidByCPFAnotherScenario() {
        var pixKey = PixKey.builder()
            .ispb(22896431)
            .key("11660117046")
            .type(KeyType.CPF)
            .branchNumber("0004")
            .accountNumber("0001098200")
            .accountType(AccountType.CHECKING)
            .taxId("11660117046")
            .name("Rodrigo Argentato")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .requestId(UUID.fromString("8a8864b6-4d97-42e3-aa9c-bab921b730c2"))
            .build();

        pixKey.calculateCid();

        final String expectedCid = "8756c87a19b85b192e4d6ea8f8747851befdbeae3474242c3580612b0743f2f8";
        assertThat(pixKey.getCid()).isEqualTo(expectedCid);
    }

    @Test
    void calculateCidByCNPJ() {
        var pixKey = PixKey.builder()
            .ispb(22896431)
            .key("89455789000189")
            .type(KeyType.CNPJ)
            .branchNumber("0004")
            .accountNumber("0001098019")
            .accountType(AccountType.CHECKING)
            .taxId("89455789000189")
            .name("Rodrigo Argentato4")
            .personType(PersonType.LEGAL_ENTITY)
            .requestId(UUID.fromString("fc0feb8d-84fc-4f22-af92-813a2b93857c"))
            .build();

        pixKey.calculateCid();

        final String expectedCid = "b2b3779042fba1a7ebec59cebdee70837c8e393782071d1c02ae33384f8c41c4";
        assertThat(pixKey.getCid()).isEqualTo(expectedCid);
    }

    @Test
    void calculateCidByCELLPHONE() {
        var pixKey = PixKey.builder()
            .ispb(22896431)
            .key("+5516920202021")
            .type(KeyType.CELLPHONE)
            .branchNumber("0001")
            .accountNumber("0003098099")
            .accountType(AccountType.CHECKING)
            .taxId("91190908034")
            .name("Rodrigo Argentato")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .requestId(UUID.fromString("8e7f5490-639f-4b98-adef-e061058c8d8d"))
            .build();

        pixKey.calculateCid();

        final String expectedCid = "a89db1a5e7beae69a15fb1c2e0dd843b2fa04f3691a9167b3a612ee9c7d187af";
        assertThat(pixKey.getCid()).isEqualTo(expectedCid);
    }

    @Test
    void calculateCidByRANDOM() {
        var pixKey = PixKey.builder()
            .ispb(22896431)
            .key("73954e40-4117-46d1-acb3-aefcf39f18e3")
            .type(KeyType.RANDOM)
            .branchNumber("0001")
            .accountNumber("0005098099")
            .accountType(AccountType.CHECKING)
            .taxId("70631471022")
            .name("Rodrigo Argentato")
            .personType(PersonType.LEGAL_ENTITY)
            .requestId(UUID.fromString("8a19d967-4bb0-40d0-aa4d-8b1e46344323"))
            .build();

        pixKey.calculateCid();

        final String expectedCid = "85d2d57820a27b74282a2768e9d766d0c01a0c26d561382b3514e483a49e7386";
        assertThat(pixKey.getCid()).isEqualTo(expectedCid);
    }

}
