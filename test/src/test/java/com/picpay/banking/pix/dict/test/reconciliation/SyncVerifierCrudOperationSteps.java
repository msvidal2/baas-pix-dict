package com.picpay.banking.pix.dict.test.reconciliation;

import com.picpay.banking.pix.dict.test.RunTests;
import com.picpay.banking.pix.dict.test.database.DatabaseUtils;
import com.picpay.banking.pix.dict.test.dictapi.DictApiRestClient;
import com.picpay.banking.pix.dict.test.sync.SyncVerifierApplicationUtil;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class SyncVerifierCrudOperationSteps extends RunTests {

    private final DatabaseUtils databaseUtils;
    private final SyncVerifierApplicationUtil syncVerifierApplicationUtil;
    private final DictApiRestClient dictApiRestClient;
    private String keyType;
    private String key;

    @Dado("que a base de dados esteja completa")
    public void thatTheDatabaseIsComplete() {
        dictApiRestClient.clearAllDataForTheTest();

        runSyncAndEnsureOK("CPF");
        runSyncAndEnsureOK("CNPJ");
        runSyncAndEnsureOK("CELLPHONE");
        runSyncAndEnsureOK("EMAIL");
        runSyncAndEnsureOK("RANDOM");
    }

    @Dado("que exista uma chave do pix do tipo {string}")
    public void thereIsAPixKeyOfTheType(String keyType) {
        this.keyType = keyType;
    }

    @Quando("uma operação de {string} é executada")
    public void anOperationIsPerformed(String operation) {
        var op = Operations.valueOf(operation);

        switch (op) {
            case CREATE:
                this.key = dictApiRestClient.cretePixKey(keyType);
                break;
            case UPDATE:
                this.key = dictApiRestClient.updatePixKey(keyType);
                break;
            case REMOVE:
                this.key = dictApiRestClient.removePixKey(keyType);
                break;
        }
    }

    @E("a sincronização entre as bases deve estar OK")
    public void theSynchronizationBetweenTheBasesMustBeOK() {
        syncVerifierApplicationUtil.run(keyType, true);
        var result = databaseUtils.findVsyncResult(keyType);
        assertThat(result).isEqualTo("OK");
    }

    private void runSyncAndEnsureOK(String keyType) {
        syncVerifierApplicationUtil.run(keyType, false);
        var result = databaseUtils.findVsyncResult(keyType);
        assertThat(result).isEqualTo("OK");
    }

    public enum Operations {
        CREATE,
        UPDATE,
        REMOVE
    }

}
