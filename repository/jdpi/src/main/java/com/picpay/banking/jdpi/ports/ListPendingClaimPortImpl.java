package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.dto.ListPendingClaimRequestDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.ListPendingClaimPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListPendingClaimPortImpl implements ListPendingClaimPort {

    private ClaimJDClient claimJDClient;

    private ListClaimConverter converter;

    @Override
    public ClaimIterable list(final Claim claim, final Integer limit, final String requestIdentifier) {

        var listClaimRequestDTO = ListPendingClaimRequestDTO.builder()
            .ispb(claim.getIspb())
            .tpPessoaLogada(claim.getPersonType().getValue())
            .cpfCnpjLogado(claim.getCpfCnpj())
            .nrAgenciaLogada(claim.getBranchNumber())
            .nrContaLogada(claim.getAccountNumber())
            .tpContaLogada(claim.getAccountType().getValue())
            .nrLimite(limit)
            .build();

        return converter.convert(claimJDClient.listPending(requestIdentifier, listClaimRequestDTO));
    }

}
