package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import org.springframework.stereotype.Service;

@Service
public class CreateClaimConverter {

    public CreateClaimRequestDTO convert(final Claim claim) {
        return CreateClaimRequestDTO.builder()
                .chave(claim.getKey())
                .cpfCnpj(claim.getCpfCnpj())
                .dtHrAberturaConta(claim.getAccountOpeningDate())
                .ispb(claim.getIspb())
                .nome(claim.getName())
                .nomeFantasia(claim.getFantasyName())
                .nrAgencia(claim.getBranchNumber())
                .nrConta(claim.getAccountNumber())
                .tpChave(claim.getKeyType().getValue())
                .tpConta(claim.getAccountType().getValue())
                .tpPessoa(claim.getPersonType().getValue())
                .tpReivindicacao(claim.getClaimType().getValue())
                .build();
    }

    public Claim convert(final Claim from, final ClaimResponseDTO responseDTO) {
        return Claim.builder()
                .accountNumber(             from.getAccountNumber())
                .accountType(               from.getAccountType())
                .branchNumber(              from.getBranchNumber())
                .claimType(                 from.getClaimType())
                .cpfCnpj(                   from.getCpfCnpj())
                .ispb(                      from.getIspb())
                .key(                       from.getKey())
                .keyType(                   from.getKeyType())
                .personType(                from.getPersonType())

                .claimSituation(            ClaimSituation.resolve(responseDTO.getClaimSituation()))
                .claimId(                   responseDTO.getClaimId())
                .lastModifiedDate(          responseDTO.getLastModifiedDate())
                .completionThresholdDate(   responseDTO.getCompletionThresholdDate())
                .resolutionThresholdDate(   responseDTO.getResolutionThresholdDate())
                .build();
    }
}
