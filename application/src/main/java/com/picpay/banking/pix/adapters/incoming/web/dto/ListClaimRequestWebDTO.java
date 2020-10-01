package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PersonType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListClaimRequestWebDTO {

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NonNull
    private Integer ispb;

    @ApiModelProperty(value = "Person type of receiver")
    private PersonType personType;

    @ApiModelProperty(value = "CPF/CNPJ of client", dataType="java.lang.String")
    private String cpfCnpj;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String")
    private String branchNumber;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String")
    private String accountNumber;

    @ApiModelProperty(value = "Account type of client")
    private AccountType accountType;

    @ApiModelProperty(value = "Limit return records", dataType="java.lang.integer", required = true)
    @NonNull
    private Integer limit;

    @ApiModelProperty(value = "Return is pending records", dataType="java.lang.boolean", required = true)
    @NonNull
    private Boolean pending;
}