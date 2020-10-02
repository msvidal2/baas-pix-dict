package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.dto.ListPendingClaimRequestDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.ListPendingClaimPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListPendingClaimPortImpl implements ListPendingClaimPort {

    private ClaimJDClient claimJDClient;

    private ListClaimConverter converter;

    @Trace
    @Override
    public ClaimIterable list(final Claim claim, final Integer limit, final String requestIdentifier) {

        var listClaimRequestDTO = ListPendingClaimRequestDTO.builder()
            .ispb(claim.getIspb())
            .tpPessoaLogada(claim.getPersonType() != null ? claim.getPersonType().getValue() : null)
            .cpfCnpjLogado(claim.getCpfCnpj() != null ? Long.parseLong(claim.getCpfCnpj()) : null)
            .nrAgenciaLogada(claim.getBranchNumber())
            .nrContaLogada(claim.getAccountNumber())
            .tpContaLogada(claim.getAccountType() != null ? claim.getAccountType().getValue() : null)
            .nrLimite(limit)
            .build();

        return converter.convert(claimJDClient.listPending(requestIdentifier, listClaimRequestDTO));
    }

}
