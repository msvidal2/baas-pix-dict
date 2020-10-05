package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.response.ListClaimDTO;
import com.picpay.banking.jdpi.dto.response.ListClaimResponseDTO;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.DonorData;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class ListClaimConverter implements DataConverter<ListClaimResponseDTO, ClaimIterable> {

    @Override
    public ClaimIterable convert(ListClaimResponseDTO from) {
        if (from.getReivindicacoesAssociadas() == null) {
            return null;
        }
        return ClaimIterable.builder()
            .claims(from.getReivindicacoesAssociadas().stream().map(this::getClaim).collect(Collectors.toList()))
            .count(from.getReivindicacoesAssociadas().size())
            .hasNext(from.getTemMaisElementos())
            .build();
    }

    private Claim getClaim(final ListClaimDTO listClaimDTO) {
        return Claim.builder()
            .claimId(listClaimDTO.getIdReivindicacao())
            .claimSituation(ClaimSituation.resolve(listClaimDTO.getStReivindicacao()))
            .claimType(ClaimType.resolve(listClaimDTO.getTpReivindicacao()))
            .participationFlow(listClaimDTO.getFluxoParticipacao())
            .key(listClaimDTO.getChave())
            .keyType(KeyType.resolve(listClaimDTO.getTpChave()))
            .ispb(listClaimDTO.getIspb())
            .accountType(AccountType.resolve(listClaimDTO.getTpConta()))
            .accountNumber(listClaimDTO.getNrConta())
            .personType(PersonType.resolve(listClaimDTO.getTpPessoa()))
            .cpfCnpj(String.valueOf(listClaimDTO.getCpfCnpj()))
            .donorIspb(listClaimDTO.getIspbDoador())
            .donorData(getDonorData(listClaimDTO))
            .accountOpeningDate(listClaimDTO.getDtHrAberturaConta())
            .resolutionThresholdDate(listClaimDTO.getDtHrLimiteResolucao())
            .completionThresholdDate(listClaimDTO.getDtHrLimiteConclusao())
            .lastModifiedDate(listClaimDTO.getDtHrUltModificacao())
            .build();
    }

    private DonorData getDonorData(final ListClaimDTO listClaimDTO) {
        DonorData donorData = null;

        if(listClaimDTO.getDoador() != null) {
             donorData = DonorData.builder()
                .branchNumber(listClaimDTO.getDoador().getNrAgencia())
                .accountType(AccountType.resolve(listClaimDTO.getDoador().getTpConta()))
                .accountNumber(listClaimDTO.getDoador().getNrConta())
                .personType(PersonType.resolve(listClaimDTO.getDoador().getTpPessoa()))
                .cpfCnpj(listClaimDTO.getDoador().getCpfCnpj())
                .name(listClaimDTO.getDoador().getNome())
                .fantasyName(listClaimDTO.getDoador().getNomeFantasia())
                .notificationDate(listClaimDTO.getDoador().getDtHrNotificacao())
                .build();
        }
        return donorData;
    }

}
