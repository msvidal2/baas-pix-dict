package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PersonType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListAddressingKeyRequestWebDTO {

    @ApiModelProperty(value = "CPF/CNPJ of client", dataType="java.lang.String", required = true)
    @NonNull
    private String cpfCnpj;

    @ApiModelProperty(value = "Person type of receiver", required = true)
    @NonNull
    private PersonType personType;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String", required = true)
    @NonNull
    private String accountNumber;

    @ApiModelProperty(value = "Account type of client", required = true)
    @NonNull
    private AccountType accountType;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String")
    private String branchNumber;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NonNull
    private Integer ispb;
}