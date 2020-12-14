//package com.picpay.banking.pix.sync;
//
//import com.picpay.banking.pix.core.domain.AccountType;
//import com.picpay.banking.pix.core.domain.KeyType;
//import com.picpay.banking.pix.core.domain.PixKey;
//import com.picpay.banking.pix.sync.domain.CidCalculator;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CidCalculatorTest {
//
//    @Test
//    @DisplayName("Calculo do CID")
//    public void calculateCid_success() {
//        String keyType = "PHONE";
//        String key = "+5511987654321";
//        String ownerTaxIdNumber = "11122233300";
//        String ownerName = "João Silva";
//        String ownerTradeName = "";
//        String participant = "12345678";
//        String branch = "00001";
//        String accountNumber = "0007654321";
//        String accountType = "CACC";
//
//        String expectedCid = "28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88";
//        String cid = CidCalculator.calculate(keyType, key, ownerTaxIdNumber, ownerName,
//            ownerTradeName, participant, branch, accountNumber, accountType);
//
//        assertThat(cid).isEqualTo(expectedCid);
//    }
//
//    @Test
//    @DisplayName("Calculo do CID 2")
//    public void calculate_success() {
//        String keyType = "PHONE";
//        String key = "+5511987654321";
//        String ownerTaxIdNumber = "11122233300";
//        String ownerName = "João Silva";
//        String ownerTradeName = "";
//        String participant = "12345678";
//        String branch = "00001";
//        String accountNumber = "0007654321";
//        String accountType = "CACC";
//
//        PixKey pixKey = PixKey.builder()
//                .type(KeyType.CELLPHONE)
//                .key("+5511987654321")
//                .taxId("11122233300")
//                .name("João Silva")
//                .fantasyName("")
//                .branchNumber("00001")
//                .accountNumber("0007654321")
//                .accountType(AccountType.CHECKING)
//                .build();
//
//        String expectedCid = "28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88";
//        String cid = CidCalculator.calculateRf(pixKey);
//
//        assertThat(cid).isEqualTo(expectedCid);
//    }
//
//}
