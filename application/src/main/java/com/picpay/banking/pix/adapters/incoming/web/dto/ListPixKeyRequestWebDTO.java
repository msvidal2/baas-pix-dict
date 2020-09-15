package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListPixKeyRequestWebDTO {

    @ApiModelProperty(value = "Value of key {CPF}/{CNPJ}/{E-mail}/{Telefone Celular}/{Aleatória}")
    @NotNull
    protected String key;

    @ApiModelProperty(value = "CPF/CNPJ of client", dataType="java.lang.String", required = true)
    @NotNull
    private String cpfCnpj;

    @ApiModelProperty(value = "Person type of receiver", required = true)
    @NotNull
    private PersonType personType;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String", required = true)
    @NotNull
    private String accountNumber;

    @ApiModelProperty(value = "Account type of client", required = true)
    @NotNull
    private AccountType accountType;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String")
    private String branchNumber;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NotNull
    private Integer ispb;

    public PixKey toDomain() {
        return PixKey.builder()
            .key(key)
            .taxId(cpfCnpj)
            .personType(personType)
            .accountNumber(accountNumber)
            .accountType(accountType)
            .branchNumber(branchNumber)
            .ispb(ispb)
            .build();
    }

}