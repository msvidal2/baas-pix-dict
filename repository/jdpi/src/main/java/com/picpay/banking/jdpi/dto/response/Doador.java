package com.picpay.banking.jdpi.dto.response;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.DonorData;
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
public class Doador {

    private String nrAgencia;
    private Integer tpConta;
    private String nrConta;
    private Integer tpPessoa;
    private Long cpfCnpj;
    private String nome;
    private String nomeFantasia;
    private LocalDateTime dtHrNotificacao;

    public DonorData toDonorData() {
        return DonorData.builder()
                    .branchNumber(nrAgencia)
                    .accountType(AccountType.resolve(tpConta))
                    .accountNumber(nrConta)
                    .personType(PersonType.resolve(tpPessoa))
                    .cpfCnpj(cpfCnpj)
                    .name(nome)
                    .fantasyName(nomeFantasia)
                    .notificationDate(dtHrNotificacao)
                    .build();
    }
}
