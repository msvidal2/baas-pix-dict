package com.picpay.banking.pix.dict.test.reconciliation;

import com.picpay.banking.pix.dict.test.database.DatabaseUtils;
import com.picpay.banking.pix.dict.test.RunTests;
import com.picpay.banking.pix.dict.test.dictapi.ReconciliationApiRestClient;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class SyncVerifierSteps extends RunTests {

    private final DatabaseUtils databaseUtils;
    private final ReconciliationApiRestClient reconciliationApiRestClient;
    private String condition;
    private String keyType;

    @Dado("que a base de dados local esteja {string}")
    public void que_a_base_de_dados_local_esteja(String condition) {
        this.condition = condition;
    }

    @Quando("acontece o sincronismo de uma chave do tipo {string}")
    public void acontece_o_sincronismo_de_uma_chave_do_tipo(String keyType) {
        this.keyType = keyType;

        if (condition.equals("vazia")) {
            databaseUtils.removeAll(keyType);
        }
    }

    @Então("o resultado esperado é {string}")
    public void o_resultado_esperado_é(String result) {
        var onlySyncVerifier = result.equals("NOK");

        if (onlySyncVerifier) {
            reconciliationApiRestClient.runOnlySyncVerifier(keyType);
        } else {
            reconciliationApiRestClient.runSyncVerifier(keyType);
        }

        String vsyncResult = databaseUtils.findVsyncResult(keyType);
        assertThat(vsyncResult).isEqualTo(result);
    }

}
