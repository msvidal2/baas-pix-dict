package com.picpay.banking.pix.original.dto.request;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.ClaimTypeOriginal;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import com.picpay.banking.pix.original.dto.PersonTypeOriginal;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateClaimRequestDTO {

    private Long accountNumber;
    private AccountTypeOriginal accountType;
    private String branch;
    private ClaimTypeOriginal claimType;
    private String key;
    private KeyTypeOriginal keyType;
    private String name;
    private String participant;
    private String taxId;
    private PersonTypeOriginal typePerson;

    public static CreateClaimRequestDTO fromClaim(final Claim claim) {
        return CreateClaimRequestDTO.builder()
                .accountNumber(Long.parseLong(claim.getAccountNumber()))
                .accountType(AccountTypeOriginal.resolveFromDomain(claim.getAccountType()))
                .branch(claim.getBranchNumber())
//                .claimType(claim.getClaimType()) // TODO: criar de para
                .key(claim.getKey())
                .keyType(KeyTypeOriginal.resolveFromDomain(claim.getKeyType()))
                .name(claim.getName())
                .participant(String.valueOf(claim.getIspb()))
                .taxId(claim.getCpfCnpj())
                .typePerson(PersonTypeOriginal.resolveFromDomain(claim.getPersonType()))
                .build();
    }

}
