package com.picpay.banking.pix.dict.test.reconciliation;

import com.picpay.banking.pix.dict.test.database.DatabaseUtils;
import com.picpay.banking.pix.dict.test.dictapi.DictApiRestClient;
import com.picpay.banking.pix.dict.test.RunTests;
import com.picpay.banking.pix.dict.test.sync.SyncVerifierApplicationUtil;
import io.cucumber.java.pt.Dado;
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
    private String operation;
    private String key;

    @Dado("que a base de dados esteja completa")
    public void que_a_base_de_dados_esteja_completa() {
        dictApiRestClient.clearAllDataForTheTest();
        syncVerifierApplicationUtil.run("CPF", false);
    }

    @Dado("que exista uma chave do pix do tipo {string}")
    public void que_exista_uma_chave_do_pix_do_tipo(String keyType) {
        this.keyType = keyType;
    }

    @Quando("uma operação de {string} é executada")
    public void uma_operação_de_é_executada(String operation) {
        this.operation = operation;
        this.key = dictApiRestClient.cretePixKey(keyType);
    }

    @Então("um evento do tipo {string} deve existir no banco de dados")
    public void um_evento_do_tipo_deve_existir_no_banco_de_dados(String string) {
        List<String> events = databaseUtils.findCidEventsByPixKey(key);
        assertThat(events).matches("ADD"::equals);
    }

    @Dado("que todas operações de CRUD foram realizadas com sucesso")
    public void que_todas_operações_de_crud_foram_realizadas_com_sucesso() {
        //syncVerifierApplicationUtil.run("CPF", false);
    }

    @Então("os 5 tipos de chaves devem estar sincronizados")
    public void os_tipos_de_chaves_devem_estar_sincronizados() {
        System.out.println("os {int} tipos de chaves devem estar sincronizados");
    }

}
