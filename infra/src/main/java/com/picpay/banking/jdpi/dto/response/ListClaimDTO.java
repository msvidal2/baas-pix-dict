package com.picpay.banking.jdpi.dto.response;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ListClaimDTO {

    private Integer tpReivindicacao;
    private Integer fluxoParticipacao;
    private String chave;
    private Integer tpChave;
    private Integer ispb;
    private String nrAgencia;
    private Integer tpConta;
    private String nrConta;
    private String nome;
    private String nomeFantasia;
    private LocalDateTime dtHrAberturaConta;
    private Integer tpPessoa;
    private Long cpfCnpj;
    private Integer ispbDoador;
    private Doador dadosDoador;
    private String idReivindicacao;
    private Integer stReivindicacao;
    private Integer motivoConfirmacao;
    private Integer motivoCancelamento;
    private LocalDateTime dtHrLimiteResolucao;
    private LocalDateTime dtHrLimiteConclusao;
    private LocalDateTime dtHrUltModificacao;

    public Claim toClaim() {
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
                .cpfCnpj(String.valueOf(cpfCnpj))
                .donorIspb(ispbDoador)
                .donorData(dadosDoador.toDonorData())
                .claimId(idReivindicacao)
                .claimSituation(ClaimSituation.resolve(stReivindicacao))
                .confirmationReason(motivoConfirmacao != null ? ClaimConfirmationReason.resolve(motivoConfirmacao) : null)
                .cancelReason(motivoCancelamento != null ? ClaimCancelReason.resolve(motivoCancelamento) : null)
                .resolutionThresholdDate(dtHrLimiteResolucao)
                .completionThresholdDate(dtHrLimiteConclusao)
                .lastModifiedDate(dtHrUltModificacao)
                .build();
    }

}
