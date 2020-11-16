package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreatePixKeyRequestWebDTO {

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NotNull
    protected KeyType type;

    @ApiModelProperty(value = "Value of key {CPF}/{CNPJ}/{E-mail}/{Telefone Celular}/{Aleatória}", required = true)
    protected String key;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NotNull
    protected Integer ispb;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String")
    protected String branchNumber;

    @ApiModelProperty(value = "Account type of client", required = true)
    @NotNull
    protected AccountType accountType;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String", required = true)
    @NotNull
    protected String accountNumber;

    @ApiModelProperty(value = "Account opening date. Format: aaaa-mm-ddTHH:mm:ss.sssZ", required = true)
    @NotNull
    protected LocalDateTime accountOpeningDate;

    @ApiModelProperty(value = "Person type of receiver", required = true)
    @NotNull
    protected PersonType personType;

    @ApiModelProperty(value = "CPF/CNPJ of client", dataType="java.lang.String", required = true)
    @NotNull
    protected String cpfCnpj;

    @ApiModelProperty(value = "Razão Social/Full client name", dataType="java.lang.String", required = true)
    @NotNull
    protected String name;

    @ApiModelProperty(value = "Client's fantasy name", dataType="java.lang.String")
    protected String fantasyName;

    @ApiModelProperty(value = "Reason for inclusion", required = true)
    @NotNull
    protected CreateReason reason;

    public PixKey toPixKey() {
        return PixKey.builder()
                .type(type)
                .key(key)
                .ispb(ispb)
                .branchNumber(branchNumber)
                .accountType(accountType)
                .accountNumber(accountNumber)
                .accountOpeningDate(accountOpeningDate)
                .personType(personType)
                .taxId(cpfCnpj)
                .name(name)
                .fantasyName(fantasyName)
                .build();
    }
}
