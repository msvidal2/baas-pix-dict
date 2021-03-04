package com.picpay.banking.pix.dict.test.reconciliation;

import com.picpay.banking.pix.dict.test.RunTests;
import com.picpay.banking.pix.dict.test.database.DatabaseUtils;
import com.picpay.banking.pix.dict.test.dictapi.DictApiRestClient;
import com.picpay.banking.pix.dict.test.dictapi.ReconciliationApiRestClient;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class SyncVerifierCrudOperationSteps extends RunTests {

    private final DatabaseUtils databaseUtils;
    private final DictApiRestClient dictApiRestClient;
    private final ReconciliationApiRestClient reconciliationApiRestClient;
    private String keyType;

    @Dado("que a base de dados esteja completa")
    public void thatTheDatabaseIsComplete() {
        dictApiRestClient.clearAllDataForTheTest();
        runSyncAndEnsureOK();
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
                dictApiRestClient.cretePixKey(keyType);
                break;
            case UPDATE:
                dictApiRestClient.updatePixKey(keyType);
                break;
            case REMOVE:
                dictApiRestClient.removePixKey(keyType);
                break;
        }
    }

    @E("a sincronização entre as bases deve estar OK")
    public void theSynchronizationBetweenTheBasesMustBeOK() {
        reconciliationApiRestClient.runOnlySyncVerifier(keyType);
        var result = databaseUtils.findVsyncResult(keyType);
        assertThat(result).isEqualTo("OK");
    }

    private void runSyncAndEnsureOK() {
        reconciliationApiRestClient.runFullSync();
        Stream.of("CPF", "CNPJ", "EMAIL", "CELLPHONE", "RANDOM")
            .forEach(keyType -> {
                var result = databaseUtils.findVsyncResult(keyType);
                assertThat(result).isEqualTo("OK");
            });
    }

    public enum Operations {
        CREATE,
        UPDATE,
        REMOVE
    }

}
