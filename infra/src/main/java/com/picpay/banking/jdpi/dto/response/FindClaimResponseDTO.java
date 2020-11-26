package com.picpay.banking.jdpi.dto.response;

import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FindClaimResponseDTO {

    private Integer tpReivindicacao;
    private Integer fluxoParticipacao;
    private String chave;
    private Integer tpChave;
    private Integer ispb;
    private String nrAgencia;
    private Integer tpConta;
    private String nrConta;
    private LocalDateTime dtHrAberturaConta;
    private Integer tpPessoa;
    private Long cpfCnpj;
    private Integer ispbDoador;
    private Doador dadosDoador;
    private String idReivindicacao;
    private Integer stReivindicacao;
    private LocalDateTime dtHrLimiteResolucao;
    private LocalDateTime dtHrLimiteConclusao;
    private LocalDateTime dtHrUltModificacao;
    private Integer motivoConfirmacao;
    private Integer motivoCancelamento;

    public Claim toClaim() {
        ClaimConfirmationReason claimConfirmationReason = null;
        ClaimCancelReason ClaimCancelReason = null;

        if (motivoConfirmacao != null)
            claimConfirmationReason = ClaimConfirmationReason.resolve(motivoConfirmacao);
        if (motivoCancelamento != null)
            ClaimCancelReason = ClaimCancelReason.resolve(motivoCancelamento);

        return Claim.builder()
                .claimType(ClaimType.resolve(tpReivindicacao))
                .participationFlow(fluxoParticipacao)
                .key(chave)
                .keyType(KeyType.resolve(tpChave))
                .ispb(ispb)
                .branchNumber(nrAgencia)
                .accountType(AccountType.resolve(tpConta))
                .accountNumber(nrConta)
                .accountOpeningDate(dtHrAberturaConta)
                .personType(PersonType.resolve(tpPessoa))
                .taxId(String.valueOf(cpfCnpj))
                .donorIspb(ispbDoador)
                .donorData((dadosDoador == null? null: dadosDoador.toDonorData()))
                .claimId(idReivindicacao)
                .claimSituation(ClaimSituation.resolve(stReivindicacao))
                .resolutionThresholdDate(dtHrLimiteResolucao)
                .completionThresholdDate(dtHrLimiteConclusao)
                .lastModifiedDate(dtHrUltModificacao)
                .cancelReason(ClaimCancelReason)
                .confirmationReason(claimConfirmationReason)
                .build();
    }
}
