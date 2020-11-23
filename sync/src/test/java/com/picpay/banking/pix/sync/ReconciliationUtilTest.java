package com.picpay.banking.pix.sync;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReconciliationUtilTest {

    @Test
    @DisplayName("Calculo do CID")
    public void calculateCid_success() {
        String keyType = "PHONE";
        String key = "+5511987654321";
        String ownerTaxIdNumber = "11122233300";
        String ownerName = "João Silva";
        String ownerTradeName = "";
        String participant = "12345678";
        String branch = "00001";
        String accountNumber = "0007654321";
        String accountType = "CACC";

        String expectedCid = "28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88";
        String cid = ReconciliationUtil.calculateCid(keyType, key, ownerTaxIdNumber, ownerName,
            ownerTradeName, participant, branch, accountNumber, accountType);

        assertThat(cid).isEqualTo(expectedCid);
    }

}
