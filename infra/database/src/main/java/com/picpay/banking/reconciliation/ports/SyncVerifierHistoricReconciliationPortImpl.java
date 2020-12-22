package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricReconciliationPort;
import com.picpay.banking.reconciliation.repository.SyncVerifierHistoricActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SyncVerifierHistoricReconciliationPortImpl implements SyncVerifierHistoricReconciliationPort {


    // TODO: falta gravar o historico do resultado. As ações feitas no banco de dados.
    private final SyncVerifierHistoricActionRepository syncVerifierHistoricActionRepository;

    @Override
    public void updateSyncVerifierHistoric(final SyncVerifierHistoric syncVerifierHistoric) {
    }

}
