package com.picpay.banking.pix.original.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import com.picpay.banking.pix.original.dto.PersonTypeOriginal;
import com.picpay.banking.pix.original.dto.ReasonOriginal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class CreateAccessKeyDTO {

    private String accountNumber;
    private String accountOpeningDate;
    private AccountTypeOriginal accountType;
    private String branch;
    private String businessPerson;
    private String key;
    private KeyTypeOriginal keyType;
    private String name;
    private ReasonOriginal reason;
    private String status;
    private String taxId;
    private PersonTypeOriginal typePerson;
    private String keyAlias;

    public static CreateAccessKeyDTO fromPixKey(final PixKey pixKey, final CreateReason reason) {
        var builder = CreateAccessKeyDTO.builder()
                .accountNumber(pixKey.getAccountNumber())
                .accountOpeningDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(pixKey.getAccountOpeningDate()))
                .accountType(AccountTypeOriginal.resolveFromDomain(pixKey.getAccountType()))
                .branch(pixKey.getBranchNumber())
                .businessPerson(pixKey.getFantasyName())
                .name(pixKey.getName())
                .reason(ReasonOriginal.resolve(reason.getValue()))
                .taxId(pixKey.getTaxId())
                .typePerson(PersonTypeOriginal.resolveFromDomain(pixKey.getPersonType()));

        if(KeyType.RANDOM.equals(pixKey.getType())) {
            builder.keyAlias("Chave EVP");
        } else {
            builder.key(pixKey.getKey())
                    .keyType(KeyTypeOriginal.resolveFromDomain(pixKey.getType()))
                    .status("ATIVO");
        }

        return builder.build();
    }

}
