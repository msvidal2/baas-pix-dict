package com.picpay.banking.pix.sync;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReconciliationUtilTest {

    @Test
    @DisplayName("Calculo do CID")
    public void calculateCid_success() {
        String keyType = "EMAIL";
        String key = "lricardolisboa@gmail.com";
        String ownerTaxIdNumber = "04812331560";
        String ownerName = "Luis Ricardo Lisboa da Silva";
        String ownerTradeName = "";
        String participant = "22896431";
        String branch = "0001";
        String accountNumber = "0007654";
        String accountType = "CACC";

        String cid = ReconciliationUtil.calculateCid(keyType, key, ownerTaxIdNumber, ownerName,
            ownerTradeName, participant, branch, accountNumber, accountType, "a946d533-7f22-42a5-9a9b-e87cd55c0f4d");

        String expectedCid = "c0f77d0aba530237f7c2defa38713ebf862d01bbe7b443342a9cf5784992cff2";
        assertThat(cid).isEqualTo(expectedCid);
    }


    @Test
    @DisplayName("Calcula o Vsync")
    public void calculateVsync_success() {
        final Set<String> cids = Set.of("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88",
            "4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f",
            "fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058");

        String vsync = ReconciliationUtil.calculateVsync(null, cids);

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync de forma cumulativa")
    public void calculateVsyncCumulative_success() {
        String vsync = ReconciliationUtil.calculateVsync(null, Set.of("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88"));
        vsync = ReconciliationUtil.calculateVsync(vsync, Set.of("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058"));
        vsync = ReconciliationUtil.calculateVsync(vsync, Set.of("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f"));

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync de cem milhões de chaves")
    @Disabled
    public void calculateVsyncPerformance_success() {
        String vsync = ReconciliationUtil.calculateVsync(null, Set.of("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88"));
        vsync = ReconciliationUtil.calculateVsync(vsync, Set.of("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058"));
        vsync = ReconciliationUtil.calculateVsync(vsync, Set.of("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f"));

        // cem milhões
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            String keyType = "PHONE";
            String key = "+5511987654321";
            String ownerTaxIdNumber = "11122233300";
            String ownerName = "João Silva " + i;
            String ownerTradeName = "";
            String participant = "12345678";
            String branch = "00001";
            String accountNumber = "0007654321";
            String accountType = "CACC";

            String cid = ReconciliationUtil.calculateCid(keyType, key, ownerTaxIdNumber, ownerName,
                ownerTradeName, participant, branch, accountNumber, accountType, "a5992013c87d9a6706b9924442d8b29e");
            vsync = ReconciliationUtil.calculateVsync(vsync, Set.of(cid));
        }
        System.out.println("Tempo para processar cem milhões de chaves em segundos: " + (System.currentTimeMillis() - startTime) / 1000);

        System.out.println(vsync);
    }

}
