package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreatePixKeyResponseJDDTO;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import org.springframework.stereotype.Component;

@Component
public class CreatePixKeyConverter {

    public CreatePixKeyRequestDTO convert(final PixKey from, final CreateReason reason) {
        return CreatePixKeyRequestDTO.builder()
                .tpChave(from.getType().getValue())
                .chave(from.getKey())
                .ispb(from.getIspb())
                .nrAgencia(from.getBranchNumber())
                .tpConta(from.getAccountType().getValue())
                .nrConta(from.getAccountNumber())
                .dtHrAberturaConta(from.getAccountOpeningDate())
                .tpPessoa(from.getPersonType().getValue())
                .cpfCnpj(Long.valueOf(from.getTaxId()))
                .nome(from.getName())
                .nomeFantasia(from.getFantasyName())
                .motivo(reason.getValue().getValue())
                .build();
    }

    public PixKey convert(final CreatePixKeyResponseJDDTO createPixKeyResponseJDDTO,
                          final PixKey from) {
        return PixKey.builder()
                .type(from.getType())
                .key(KeyType.EVP.equals(from.getType()) ? createPixKeyResponseJDDTO.getChave() : from.getKey())
                .ispb(from.getIspb())
                .branchNumber(from.getBranchNumber())
                .accountType(from.getAccountType())
                .accountNumber(from.getAccountNumber())
                .accountOpeningDate(from.getAccountOpeningDate())
                .personType(from.getPersonType())
                .taxId(from.getTaxId())
                .name(from.getName())
                .fantasyName(from.getFantasyName())
                .accountOpeningDate(from.getAccountOpeningDate())
                .createdAt(createPixKeyResponseJDDTO.getDtHrCriacaoChave())
                .startPossessionAt(createPixKeyResponseJDDTO.getDtHrInicioPosseChave())
                .build();
    }

}
