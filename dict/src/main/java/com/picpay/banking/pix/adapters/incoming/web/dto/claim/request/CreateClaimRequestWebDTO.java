package com.picpay.banking.pix.adapters.incoming.web.dto.claim.request;

import com.picpay.banking.pix.core.domain.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateClaimRequestWebDTO {

    @ApiModelProperty(value = "Type of Claim: Portabilidade or Reivindicacao de Posse", required = true)
    @NotNull
    private ClaimType claimType;

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NotNull
    private KeyType keyType;

    @ApiModelProperty(value = "Value of key {CPF}/{CNPJ}/{E-mail}/{Telefone Celular}/{Aleatória}", required = true)
    @NotNull
    private String key;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    private int ispb;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String")
    private String branchNumber;

    @ApiModelProperty(value = "Account type of client", required = true)
    @NotNull
    private AccountType accountType;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String", required = true)
    @NotNull
    private String accountNumber;

    @ApiModelProperty(value = "Person type of receiver", required = true)
    @NotNull
    private PersonType personType;

    @ApiModelProperty(value = "Account opening date. Format: aaaa-mm-ddTHH:mm:ss.sssZ", required = true)
    @NotNull
    protected LocalDateTime accountOpeningDate;

    @ApiModelProperty(value = "CPF/CNPJ of client", dataType="java.lang.String", required = true)
    @NotNull
    private String cpfCnpj;

    @ApiModelProperty(value = "Razão Social/Full client name", dataType="java.lang.String", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(value = "Client's fantasy name", dataType="java.lang.String")
    private String fantasyName;

    public Claim toDomain() {
        return Claim.builder()
                .accountNumber(accountNumber)
                .accountType(accountType)
                .branchNumber(branchNumber)
                .claimType(claimType)
                .cpfCnpj(cpfCnpj)
                .ispb(ispb)
                .pixKey(new PixKey(key, keyType))
                .personType(personType)
                .name(name)
                .fantasyName(fantasyName)
                .accountOpeningDate(accountOpeningDate)
                .build();
    }

}
