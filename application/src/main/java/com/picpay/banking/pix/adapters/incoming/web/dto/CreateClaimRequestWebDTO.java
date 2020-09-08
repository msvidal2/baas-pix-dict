package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateClaimRequestWebDTO {

    @ApiModelProperty(value = "Type of Claim: Portabilidade or Reivindicacao de Posse", required = true)
    @NonNull
    private ClaimType claimType;

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NonNull
    private KeyType keyType;

    @ApiModelProperty(value = "Value of key {CPF}/{CNPJ}/{E-mail}/{Telefone Celular}/{EVP}", required = true)
    @NonNull
    private String key;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    private int ispb;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String")
    private String branchNumber;

    @ApiModelProperty(value = "Account type of client", required = true)
    @NonNull
    private AccountType accountType;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String", required = true)
    @NonNull
    private String accountNumber;

    @ApiModelProperty(value = "Person type of receiver", required = true)
    @NonNull
    private PersonType personType;

    @ApiModelProperty(value = "Account opening date. Format: aaaa-mm-ddTHH:mm:ss.sssZ", required = true)
    @NonNull
    protected LocalDateTime accountOpeningDate;

    @ApiModelProperty(value = "CPF/CNPJ of client", dataType="java.lang.String", required = true)
    private long cpfCnpj;

    @ApiModelProperty(value = "Razão Social/Full client name", dataType="java.lang.String", required = true)
    @NonNull
    private String name;

    @ApiModelProperty(value = "Client's fantasy name", dataType="java.lang.String")
    private String fantasyName;

    @ApiModelProperty(value = "We suggest using UUID (v4) typing, 36 characters long.", required = true)
    @NonNull
    private String requestIdentifier;
}
