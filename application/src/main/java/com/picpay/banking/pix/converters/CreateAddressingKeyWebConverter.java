package com.picpay.banking.pix.converters;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import org.springframework.stereotype.Component;

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

}
