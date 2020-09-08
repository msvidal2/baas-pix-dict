package com.picpay.banking.pix.converters;

import com.picpay.banking.jdpi.dto.request.CreateAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateAddressingKeyResponseJDDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreateAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ListKeyResponseWebDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.CreateReason;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CreateAddressingKeyWebConverter implements DataConverter<CreateAddressingKeyRequestWebDTO, AddressingKey> {

    @Override
    public AddressingKey convert(final CreateAddressingKeyRequestWebDTO requestDTO) {
        return AddressingKey.builder()
            .type(requestDTO.getType())
            .key(requestDTO.getKey())
            .ispb(requestDTO.getIspb())
            .branchNumber(requestDTO.getBranchNumber())
            .accountType(requestDTO.getAccountType())
            .accountNumber(requestDTO.getAccountNumber())
            .accountOpeningDate(requestDTO.getAccountOpeningDate())
            .personType(requestDTO.getPersonType())
            .cpfCnpj(Long.valueOf(requestDTO.getCpfCnpj()).longValue())
            .name(requestDTO.getName())
            .fantasyName(requestDTO.getFantasyName())
            .accountOpeningDate(requestDTO.getAccountOpeningDate())
            .build();
    }

    public CreateAddressingKeyRequestDTO convert(final AddressingKey from, final CreateReason reason) {
        return CreateAddressingKeyRequestDTO.builder()
            .tpChave(from.getType().getValue())
            .chave(from.getKey())
            .ispb(from.getIspb())
            .nrAgencia(from.getBranchNumber())
            .tpConta(from.getAccountType().getValue())
            .nrConta(from.getAccountNumber())
            .dtHrAberturaConta(from.getAccountOpeningDate())
            .tpPessoa(from.getPersonType().getValue())
            .cpfCnpj(from.getCpfCnpj())
            .nome(from.getName())
            .nomeFantasia(from.getFantasyName())
            .motivo(reason.getValue())
            .build();
    }

    public AddressingKey convert(final CreateAddressingKeyResponseJDDTO createAddressingKeyResponseJDDTO,
        final AddressingKey from) {
        return AddressingKey.builder()
            .type(from.getType())
            .key(from.getKey())
            .ispb(from.getIspb())
            .branchNumber(from.getBranchNumber())
            .accountType(from.getAccountType())
            .accountNumber(from.getAccountNumber())
            .accountOpeningDate(from.getAccountOpeningDate())
            .personType(from.getPersonType())
            .cpfCnpj(from.getCpfCnpj())
            .name(from.getName())
            .fantasyName(from.getFantasyName())
            .accountOpeningDate(from.getAccountOpeningDate())
            .createdAt(createAddressingKeyResponseJDDTO.getDtHrCriacaoChave())
            .startPossessionAt(createAddressingKeyResponseJDDTO.getDtHrInicioPosseChave())

            .build();
    }

    public Collection<ListKeyResponseWebDTO> convert(final Collection<AddressingKey> from) {
        return from.stream().map(this::getAddressingKey).collect(Collectors.toList());
    }

    private ListKeyResponseWebDTO getAddressingKey(final AddressingKey addressingKey) {
        return ListKeyResponseWebDTO.builder()
            .key(addressingKey.getKey())
            .name(addressingKey.getName())
            .fantasyName(addressingKey.getFantasyName())
            .createdAt(addressingKey.getCreatedAt())
            .startPossessionAt(addressingKey.getStartPossessionAt())
            .claim(addressingKey.getClaim().getValue())
            .build();
    }

}
