package com.picpay.banking.pix.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.createContentIdentifier;
import static org.assertj.core.api.Assertions.assertThat;

class SyncVerifierTest {

    @Test
    @DisplayName("Calcula o Vsync com sucesso seguindo exemplo do BACEN")
    void calculateVsync_success() {
        final List<ContentIdentifierEvent> contentIdentifiers = List.of(
            createContentIdentifier("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88"),
            createContentIdentifier("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f"),
            createContentIdentifier("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058"));

        SyncVerifier syncVerifier = SyncVerifier.builder().build();

        String vsync = syncVerifier.calculateVsync(contentIdentifiers);

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync de forma cumulativa")
    void calculateVsyncCumulative_success() {
        var vsync = SyncVerifier.builder().build()
            .calculateVsync(List.of(createContentIdentifier("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88")));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of(createContentIdentifier("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058")));

        vsync = SyncVerifier.builder().vsync(vsync).build()
            .calculateVsync(List.of(createContentIdentifier("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f")));

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync usando dados reais coletados via txt")
    void calculates_vsync_with_real_data() throws FileNotFoundException {
        FileReader evpTxt = new FileReader("src/test/java/com/picpay/banking/pix/core/domain/evp.txt");
        Scanner fileReaderScan = new Scanner(evpTxt);

        List<ContentIdentifierEvent> contentIdentifiers = new ArrayList<>();
        while (fileReaderScan.hasNextLine()) {
            contentIdentifiers.add(createContentIdentifier(fileReaderScan.next()));
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

        var syncVerifierHistoric = syncVerifier.syncVerificationResult(vsyncEnd, SyncVerifierResult.OK);

        assertThat(syncVerifier.getSyncVerifierResult()).isEqualTo(SyncVerifierResult.OK);
        assertThat(syncVerifier.getSynchronizedAt()).isNotEqualTo(lastSyncDone);
        assertThat(syncVerifier.isOK()).isTrue();
        assertThat(syncVerifier.getVsync()).isEqualTo(vsyncEnd);

        assertThat(syncVerifierHistoric.getSyncVerifierResult()).isEqualTo(SyncVerifierResult.OK);
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

        var syncVerifierHistoric = syncVerifier.syncVerificationResult(vsyncEnd, SyncVerifierResult.NOK);

        assertThat(syncVerifier.getSyncVerifierResult()).isEqualTo(SyncVerifierResult.NOK);
        assertThat(syncVerifier.getSynchronizedAt()).isEqualTo(lastSyncDone);
        assertThat(syncVerifier.isNOk()).isTrue();
        assertThat(syncVerifier.getVsync()).isEqualTo(vsyncStart);

        assertThat(syncVerifierHistoric.getSyncVerifierResult()).isEqualTo(SyncVerifierResult.NOK);
        assertThat(syncVerifierHistoric.getSynchronizedStart()).isEqualTo(lastSyncDone);
        assertThat(syncVerifierHistoric.getSynchronizedEnd()).isAfter(lastSyncDone);
        assertThat(syncVerifierHistoric.getVsyncStart()).isEqualTo(vsyncStart);
        assertThat(syncVerifierHistoric.getVsyncEnd()).isEqualTo(vsyncEnd);
        assertThat(syncVerifierHistoric.getKeyType()).isEqualTo(syncVerifier.getKeyType());
    }

}
