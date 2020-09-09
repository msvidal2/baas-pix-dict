package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.dto.request.ListClaimRequestDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.ListClaimPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListClaimPortImpl implements ListClaimPort {

    private ClaimJDClient claimJDClient;

    private ListClaimConverter converter;

    @Override
    public ClaimIterable list(final Claim claim, final Integer limit, final String requestIdentifier) {

        var listClaimRequestDTO = ListClaimRequestDTO.builder()
            .ispb(claim.getIspb())
            .tpPessoaLogada(claim.getPersonType() != null ? claim.getPersonType().getValue() : null)
            .cpfCnpjLogado(claim.getCpfCnpj())
            .nrAgenciaLogada(claim.getBranchNumber())
            .nrContaLogada(claim.getAccountNumber())
            .tpContaLogada(claim.getAccountType() != null ? claim.getAccountType().getValue() : null)
            .nrLimite(limit)
            .build();

        return converter.convert(claimJDClient.list(requestIdentifier, listClaimRequestDTO));
    }

}
