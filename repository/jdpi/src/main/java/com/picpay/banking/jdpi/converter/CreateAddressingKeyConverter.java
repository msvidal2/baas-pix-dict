package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.request.CreateAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateAddressingKeyResponseJDDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.CreateReason;
import org.springframework.stereotype.Component;

@Component
public class CreateAddressingKeyConverter {

    public CreateAddressingKeyRequestDTO convert(final AddressingKey from, final CreateReason reason) {
        return CreateAddressingKeyRequestDTO.builder()
                .tpChave(           from.getType().getValue())
                .chave(             from.getKey())
                .ispb(              from.getIspb())
                .nrAgencia(         from.getBranchNumber())
                .tpConta(           from.getAccountType().getValue())
                .nrConta(           from.getAccountNumber())
                .dtHrAberturaConta( from.getAccountOpeningDate())
                .tpPessoa(          from.getPersonType().getValue())
                .cpfCnpj(           from.getCpfCnpj())
                .nome(              from.getName())
                .nomeFantasia(      from.getFantasyName())
                .motivo(            reason.getValue())
                .build();
    }

    public AddressingKey convert(final CreateAddressingKeyResponseJDDTO createAddressingKeyResponseJDDTO,
                                 final AddressingKey from) {
        return AddressingKey.builder()
                .type(                  from.getType())
                .key(                   from.getKey())
                .ispb(                  from.getIspb())
                .branchNumber(          from.getBranchNumber())
                .accountType(           from.getAccountType())
                .accountNumber(         from.getAccountNumber())
                .accountOpeningDate(    from.getAccountOpeningDate())
                .personType(            from.getPersonType())
                .cpfCnpj(               from.getCpfCnpj())
                .name(                  from.getName())
                .fantasyName(           from.getFantasyName())
                .accountOpeningDate(    from.getAccountOpeningDate())
                .createdAt(             createAddressingKeyResponseJDDTO.getDtHrCriacaoChave())
                .startPossessionAt(     createAddressingKeyResponseJDDTO.getDtHrInicioPosseChave())

                .build();
    }
}
