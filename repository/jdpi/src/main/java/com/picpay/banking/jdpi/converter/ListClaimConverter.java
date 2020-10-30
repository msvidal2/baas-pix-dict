package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.response.ListClaimDTO;
import com.picpay.banking.jdpi.dto.response.ListClaimResponseDTO;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.DonorData;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class ListClaimConverter implements DataConverter<ListClaimResponseDTO, ClaimIterable> {

    @Override
    public ClaimIterable convert(ListClaimResponseDTO from) {
        Collection<ListClaimDTO> result = null;

        result = from.getReivindicacoes() !=null ? from.getReivindicacoes() : from.getReivindicacoesAssociadas();

        if (result == null) {
            return null;
        }

        return ClaimIterable.builder()
            .claims(result.stream().map(this::getClaim).collect(Collectors.toList()))
            .count(result.size())
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
            .name(listClaimDTO.getNome())
            .fantasyName(listClaimDTO.getNomeFantasia())
            .accountType(AccountType.resolve(listClaimDTO.getTpConta()))
            .accountNumber(listClaimDTO.getNrConta())
            .branchNumber(listClaimDTO.getNrAgencia())
            .personType(PersonType.resolve(listClaimDTO.getTpPessoa()))
            .cpfCnpj(String.valueOf(listClaimDTO.getCpfCnpj()))
            .donorIspb(listClaimDTO.getIspbDoador())
            .confirmationReason(listClaimDTO.getMotivoConfirmacao() != null ? ClaimConfirmationReason.resolve(listClaimDTO.getMotivoConfirmacao()) : null)
            .cancelReason(listClaimDTO.getMotivoCancelamento() != null ? ClaimCancelReason.resolve(listClaimDTO.getMotivoCancelamento()) : null)
            .donorData(getDonorData(listClaimDTO))
            .accountOpeningDate(listClaimDTO.getDtHrAberturaConta())
            .resolutionThresholdDate(listClaimDTO.getDtHrLimiteResolucao())
            .completionThresholdDate(listClaimDTO.getDtHrLimiteConclusao())
            .lastModifiedDate(listClaimDTO.getDtHrUltModificacao())
            .build();
    }

    private DonorData getDonorData(final ListClaimDTO listClaimDTO) {
        DonorData donorData = null;

        if(listClaimDTO.getDadosDoador() != null) {
             donorData = DonorData.builder()
                .branchNumber(listClaimDTO.getDadosDoador().getNrAgencia())
                .accountType(AccountType.resolve(listClaimDTO.getDadosDoador().getTpConta()))
                .accountNumber(listClaimDTO.getDadosDoador().getNrConta())
                .personType(PersonType.resolve(listClaimDTO.getDadosDoador().getTpPessoa()))
                .cpfCnpj(listClaimDTO.getDadosDoador().getCpfCnpj())
                .name(listClaimDTO.getDadosDoador().getNome())
                .fantasyName(listClaimDTO.getDadosDoador().getNomeFantasia())
                .notificationDate(listClaimDTO.getDadosDoador().getDtHrNotificacao())
                .build();
        }
        return donorData;
    }

}
