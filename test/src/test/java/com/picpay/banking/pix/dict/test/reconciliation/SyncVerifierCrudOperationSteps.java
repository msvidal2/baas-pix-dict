package com.picpay.banking.pix.dict.test.reconciliation;

import com.picpay.banking.pix.dict.test.RunTests;
import com.picpay.banking.pix.dict.test.database.DatabaseUtils;
import com.picpay.banking.pix.dict.test.dictapi.DictApiRestClient;
import com.picpay.banking.pix.dict.test.sync.SyncVerifierApplicationUtil;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class SyncVerifierCrudOperationSteps extends RunTests {

    private final DatabaseUtils databaseUtils;
    private final SyncVerifierApplicationUtil syncVerifierApplicationUtil;
    private final DictApiRestClient dictApiRestClient;
    private String keyType;
    private String key;

    @Dado("que a base de dados esteja completa")
    public void que_a_base_de_dados_esteja_completa() {
        dictApiRestClient.clearAllDataForTheTest();

        runSyncAndEnsureOK("CPF");
//        runSyncAndEnsureOK("CNPJ");
//        runSyncAndEnsureOK("CELLPHONE");
//        runSyncAndEnsureOK("EMAIL");
//        runSyncAndEnsureOK("RANDOM");
    }

    @Dado("que exista uma chave do pix do tipo {string}")
    public void que_exista_uma_chave_do_pix_do_tipo(String keyType) {
        this.keyType = keyType;
    }

    @Quando("uma operação de {string} é executada")
    public void uma_operação_de_é_executada(String operation) {
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

    @Então("um evento do tipo {string} deve existir no banco de dados")
    public void um_evento_do_tipo_deve_existir_no_banco_de_dados(String eventType) {
        List<String> events = databaseUtils.findCidEventsByPixKey(key);

        if ("ADD + REMOVE".equals(eventType)) {
            assertThat(events.contains("ADD")).isTrue();
            assertThat(events.contains("REMOVE")).isTrue();
        } else {
            assertThat(events.contains(eventType)).isTrue();
        }
    }

    @E("a sincronização entre as bases deve estar OK")
    public void a_sincronização_entre_as_bases_deve_estar_ok() {
        syncVerifierApplicationUtil.run(keyType, true);
        var result = databaseUtils.findVsyncResult(keyType);
        assertThat(result).isEqualTo("OK");
    }

    @Dado("a sincronização de base seja executada")
    public void a_sincronização_de_base_seja_executada() {
        syncVerifierApplicationUtil.run("CPF", true);
//        syncVerifierApplicationUtil.run("CNPJ", true);
//        syncVerifierApplicationUtil.run("CELLPHONE", true);
//        syncVerifierApplicationUtil.run("EMAIL", true);
//        syncVerifierApplicationUtil.run("RANDOM", true);
    }

    @Então("os 5 tipos de chaves devem estar sincronizados")
    public void os_tipos_de_chaves_devem_estar_sincronizados() {
        var cpfResult = databaseUtils.findVsyncResult("CPF");
//        var cnpjResult = databaseUtils.findVsyncResult("CNPJ");
//        var cellPhoneResult = databaseUtils.findVsyncResult("CELLPHONE");
//        var emailResult = databaseUtils.findVsyncResult("EMAIL");
//        var randomResult = databaseUtils.findVsyncResult("RANDOM");

        assertThat(cpfResult).isEqualTo("OK");
//        assertThat(cnpjResult).isEqualTo("OK");
//        assertThat(cellPhoneResult).isEqualTo("OK");
//        assertThat(emailResult).isEqualTo("OK");
//        assertThat(randomResult).isEqualTo("OK");
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
