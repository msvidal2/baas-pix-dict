package com.picpay.banking.pix.converters;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateClaimRequestWebDTO;
import com.picpay.banking.pix.core.domain.Claim;
import org.springframework.stereotype.Component;

@Component
public class CreateClaimWebConverter implements DataConverter<CreateClaimRequestWebDTO, Claim> {


    @Override
    public Claim convert(CreateClaimRequestWebDTO from) {
        return Claim.builder()
            .accountNumber(from.getAccountNumber())
            .accountType(from.getAccountType())
            .branchNumber(from.getBranchNumber())
            .claimType(from.getClaimType())
            .taxId(from.getCpfCnpj())
            .ispb(from.getIspb())
            .key(from.getKey())
            .keyType(from.getKeyType())
            .personType(from.getPersonType())
            .name(from.getName())
            .fantasyName(from.getFantasyName())
            .accountOpeningDate(from.getAccountOpeningDate())
            .build();
    }

}
