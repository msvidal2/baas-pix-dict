package com.picpay.banking.pix.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class SyncVerifierTest {

    @Test
    @DisplayName("Calcula o Vsync com sucesso seguindo exemplo do BACEN")
    void calculateVsync_success() {
        final List<String> contentIdentifiers = List.of(
            "28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88",
            "4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f",
            "fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058");

        SyncVerifier syncVerifier = SyncVerifier.builder().build();

        String vsync = syncVerifier.calculateVsync(contentIdentifiers);

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync com sucesso seguindo um caso real de cpf")
    void calculateVsync_success_cpf() {
        final List<String> contentIdentifiers = List.of(
            "b790523c9140c6179a903414e511411bf7ebb5308dd40237fe0e4fe8b83219c5",
            "3cffe4768f1deba26a5e097d1f12a18f530c2542f367e39d4781a97cf2437423",
            "5428b8076a310f82985e5cec9e046bb92ae17854f05aba63b0ed709c43ffa9ed",
            "85de03695a98e4ee0434bc40255e375e7fd0849f2a2a2143f58d31fd04fd2da4");

        SyncVerifier syncVerifier = SyncVerifier.builder().build();

        String vsync = syncVerifier.calculateVsync(contentIdentifiers);

        String expectedVsync = "5a990d242ef4c6d96ca4ddc54159bc73f1d66cb9a4c37a8afcefa7f50d73e9af";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync com sucesso seguindo um caso real de EMAIL")
    void calculateVsync_success_email() {
        final List<String> contentIdentifiers = List.of(
            "967c242cd276e3bec7647c5066fb946c59075abb4181eb66e808648a9bcbfa66",
            "3188ccd11484acfd185ca65ccf14f29ccae5a1344f42e19369fc18ffbc801c4f",
            "3738b56cfdf7c779768e201a1e93168debd6e15e01d9fb066ec9a7060d1345c7",
            "6faf865efec3d0be26c0cc682dc19b2408520b72e9a450063a05269233a812d2",
            "3766f705fc14118ab2139eedf2f07355632af98054b7a48311da4f8c01278374",
            "dd8f70aaef821fa72517bd747d889549d3d28bc03776f989c567e4ad4cf80c10",
            "452c144eec3cd91f9d689b1ec2c0e1bfc6334731cc0a03f26662a93cc5abb6b0",
            "f53f24b31e1d73b8c936d3afe02021662b5ace2bb3a749bb8d7c02928ce8db2c");

        SyncVerifier syncVerifier = SyncVerifier.builder().build();

        String vsync = syncVerifier.calculateVsync(contentIdentifiers);

        String expectedVsync = "a5996c9d2471fc0e4c2c5d563725cd9c2df7eaf9fad2b6b6ea9bfd6e1d6c53c4";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync com sucesso quando existe apenas um Cid")
    void calculateVsync_only_one_cid() {
        final List<String> contentIdentifiers = List.of(
            "b2b3779042fba1a7ebec59cebdee70837c8e393782071d1c02ae33384f8c41c4");

        SyncVerifier syncVerifier = SyncVerifier.builder().build();

        String vsync = syncVerifier.calculateVsync(contentIdentifiers);

        String expectedVsync = "b2b3779042fba1a7ebec59cebdee70837c8e393782071d1c02ae33384f8c41c4";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync de forma cumulativa")
    void calculateVsyncCumulative_success() {
        var vsync = SyncVerifier.builder().build()
            .calculateVsync(List.of("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88"));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058"));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f"));

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync com eventos de add e remove para o mesmo cid, um por vez")
    void calculate_vsync_add_remove_same_cid_one_at_time_success() {
        var startVsync = "2fa01a1acd47bd8d7318ba50ec839dd22eccd5a2564ddbf9dc5684fb64c350b9";

        var vsync = SyncVerifier.builder().vsync(startVsync).build()
            .calculateVsync(List.of("e4bf462017a6480a906689daca85d4e4deaf0745302d39a94db034fcd5cfc437"));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of("e4bf462017a6480a906689daca85d4e4deaf0745302d39a94db034fcd5cfc437"));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of("e4bf462017a6480a906689daca85d4e4deaf0745302d39a94db034fcd5cfc437"));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of("a28919eb0499ba784b4955c0f786fe1a9918e56648659ba44db76e1615b03d4c"));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of("a28919eb0499ba784b4955c0f786fe1a9918e56648659ba44db76e1615b03d4c"));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of("f8a7a19c56239b2388a1bb7d54321877d6f2e8dfa3969e933996df6e80afe088"));

        String expectedVsync = "33b8fda68cc26ea46bdf88f77234514126913a38c5f67cc3a8706f6931a37406";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync com eventos de add e remove para o mesmo cid, todos de uma vez")
    void calculate_vsync_add_remove_same_cid_with_list_success() {
        var startVsync = "2fa01a1acd47bd8d7318ba50ec839dd22eccd5a2564ddbf9dc5684fb64c350b9";

        var cids = List.of("e4bf462017a6480a906689daca85d4e4deaf0745302d39a94db034fcd5cfc437",
            "e4bf462017a6480a906689daca85d4e4deaf0745302d39a94db034fcd5cfc437",
            "e4bf462017a6480a906689daca85d4e4deaf0745302d39a94db034fcd5cfc437",
            "a28919eb0499ba784b4955c0f786fe1a9918e56648659ba44db76e1615b03d4c",
            "a28919eb0499ba784b4955c0f786fe1a9918e56648659ba44db76e1615b03d4c",
            "f8a7a19c56239b2388a1bb7d54321877d6f2e8dfa3969e933996df6e80afe088");

        var vsync = SyncVerifier.builder().vsync(startVsync).build().calculateVsync(cids);

        String expectedVsync = "33b8fda68cc26ea46bdf88f77234514126913a38c5f67cc3a8706f6931a37406";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync usando dados reais coletados via txt")
    void calculates_vsync_with_real_data() throws FileNotFoundException {
        FileReader evpTxt = new FileReader("src/test/java/com/picpay/banking/pix/core/domain/evp.txt");
        Scanner fileReaderScan = new Scanner(evpTxt);

        List<String> contentIdentifiers = new ArrayList<>();
        while (fileReaderScan.hasNextLine()) {
            contentIdentifiers.add(fileReaderScan.next());
        }

        SyncVerifier syncVerifier = SyncVerifier.builder().build();

        String vsync = syncVerifier.calculateVsync(contentIdentifiers);

        String expectedVsync = "7a15a318271483cddd00f9b81c3470849ef09c16517657fd2137cc490d1e149e";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Quando OK, define o resultado e atualiza a data do sincronismo")
    void when_resultOK_expected_synchronizedAt_updated() {
        final LocalDateTime lastSyncDone = LocalDateTime.now();
        final String vsyncStart = "vsyncStart";
        final String vsyncEnd = "vsyncEnd";

        SyncVerifier syncVerifier = SyncVerifier.builder()
            .vsync(vsyncStart)
            .synchronizedAt(lastSyncDone)
            .build();

        var resultOK = SyncVerifierResult.builder()
            .syncVerifierLastModified(LocalDateTime.now())
            .syncVerifierResultType(SyncVerifierResultType.OK)
            .build();
        var syncVerifierHistoric = syncVerifier.syncVerificationResult(vsyncEnd, resultOK);

        assertThat(syncVerifier.getSyncVerifierResultType()).isEqualTo(SyncVerifierResultType.OK);
        assertThat(syncVerifier.getSynchronizedAt()).isNotEqualTo(lastSyncDone);
        assertThat(syncVerifier.isOK()).isTrue();
        assertThat(syncVerifier.getVsync()).isEqualTo(vsyncEnd);

        assertThat(syncVerifierHistoric.getSyncVerifierResultType()).isEqualTo(SyncVerifierResultType.OK);
        assertThat(syncVerifierHistoric.getSynchronizedStart()).isEqualTo(lastSyncDone);
        assertThat(syncVerifierHistoric.getSynchronizedEnd()).isEqualTo(syncVerifier.getSynchronizedAt());
        assertThat(syncVerifierHistoric.getVsyncStart()).isEqualTo(vsyncStart);
        assertThat(syncVerifierHistoric.getVsyncEnd()).isEqualTo(vsyncEnd);
        assertThat(syncVerifierHistoric.getKeyType()).isEqualTo(syncVerifier.getKeyType());
    }

    @Test
    @DisplayName("Quando NOK, define o resultado e mantêm a data do ultimo sincronismo")
    void when_resultNOK_expected_synchronizedAt_notUpdated() {
        final LocalDateTime lastSyncDone = LocalDateTime.now();
        final String vsyncStart = "vsyncStart";
        final String vsyncEnd = "vsyncEnd";

        SyncVerifier syncVerifier = SyncVerifier.builder()
            .vsync(vsyncStart)
            .synchronizedAt(lastSyncDone)
            .build();

        var resultNOK = SyncVerifierResult.builder()
            .syncVerifierLastModified(LocalDateTime.now())
            .syncVerifierResultType(SyncVerifierResultType.NOK)
            .build();
        var syncVerifierHistoric = syncVerifier.syncVerificationResult(vsyncEnd, resultNOK);

        assertThat(syncVerifier.getSyncVerifierResultType()).isEqualTo(SyncVerifierResultType.NOK);
        assertThat(syncVerifier.getSynchronizedAt()).isEqualTo(lastSyncDone);
        assertThat(syncVerifier.isNOk()).isTrue();
        assertThat(syncVerifier.getVsync()).isEqualTo(vsyncStart);

        assertThat(syncVerifierHistoric.getSyncVerifierResultType()).isEqualTo(SyncVerifierResultType.NOK);
        assertThat(syncVerifierHistoric.getSynchronizedStart()).isEqualTo(lastSyncDone);
        assertThat(syncVerifierHistoric.getSynchronizedEnd()).isAfter(lastSyncDone);
        assertThat(syncVerifierHistoric.getVsyncStart()).isEqualTo(vsyncStart);
        assertThat(syncVerifierHistoric.getVsyncEnd()).isEqualTo(vsyncEnd);
        assertThat(syncVerifierHistoric.getKeyType()).isEqualTo(syncVerifier.getKeyType());
    }

}
