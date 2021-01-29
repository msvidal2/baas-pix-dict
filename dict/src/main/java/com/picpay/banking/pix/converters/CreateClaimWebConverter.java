package com.picpay.banking.pix.converters;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateClaimRequestWebDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.PixKey;
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
            .cpfCnpj(from.getCpfCnpj())
            .ispb(from.getIspb())
            .pixKey(new PixKey(from.getKey(), from.getKeyType()))
            .personType(from.getPersonType())
            .name(from.getName())
            .fantasyName(from.getFantasyName())
            .accountOpeningDate(from.getAccountOpeningDate())
            .build();
    }

}
