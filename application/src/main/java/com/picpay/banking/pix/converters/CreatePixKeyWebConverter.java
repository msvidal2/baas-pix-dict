package com.picpay.banking.pix.converters;

import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreatePixKeyResponseJDDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreatePixKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ListKeyResponseWebDTO;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CreatePixKeyWebConverter implements DataConverter<CreatePixKeyRequestWebDTO, PixKey> {

    @Override
    public PixKey convert(final CreatePixKeyRequestWebDTO requestDTO) {
        return PixKey.builder()
            .type(requestDTO.getType())
            .key(requestDTO.getKey())
            .ispb(requestDTO.getIspb())
            .branchNumber(requestDTO.getBranchNumber())
            .accountType(requestDTO.getAccountType())
            .accountNumber(requestDTO.getAccountNumber())
            .accountOpeningDate(requestDTO.getAccountOpeningDate())
            .personType(requestDTO.getPersonType())
            .taxId(Long.valueOf(requestDTO.getCpfCnpj()).longValue())
            .name(requestDTO.getName())
            .fantasyName(requestDTO.getFantasyName())
            .accountOpeningDate(requestDTO.getAccountOpeningDate())
            .build();
    }

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
            .cpfCnpj(from.getTaxId())
            .nome(from.getName())
            .nomeFantasia(from.getFantasyName())
            .motivo(reason.getValue())
            .build();
    }

    public PixKey convert(final CreatePixKeyResponseJDDTO createPixKeyResponseJDDTO,
                          final PixKey from) {
        return PixKey.builder()
            .type(from.getType())
            .key(from.getKey())
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

    public Collection<ListKeyResponseWebDTO> convert(final Collection<PixKey> from) {
        return from.stream()
                .map(this::getPixKey)
                .collect(Collectors.toList());
    }

    private ListKeyResponseWebDTO getPixKey(final PixKey pixKey) {
        return ListKeyResponseWebDTO.builder()
            .key(pixKey.getKey())
            .name(pixKey.getName())
            .fantasyName(pixKey.getFantasyName())
            .createdAt(pixKey.getCreatedAt())
            .startPossessionAt(pixKey.getStartPossessionAt())
            .claim(pixKey.getClaim() != null ? pixKey.getClaim().getValue() : null)
            .build();
    }

}
