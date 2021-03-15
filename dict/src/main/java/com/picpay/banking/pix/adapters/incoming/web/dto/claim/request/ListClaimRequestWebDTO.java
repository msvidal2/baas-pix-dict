package com.picpay.banking.pix.adapters.incoming.web.dto.claim.request;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.PersonType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListClaimRequestWebDTO {

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
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

    @ApiModelProperty(value = "Start date: ISO Format (yyyy-MM-dd'T'HH:mm:ss.SSS)", required = true)
    @NotNull
    private String startDate;

    @ApiModelProperty(value = "End date: ISO Format (yyyy-MM-dd'T'HH:mm:ss.SSS)")
    private String endDate;

    @ApiModelProperty(value = "Limit return records", dataType="java.lang.integer", required = true)
    @NotNull
    private Integer limit;

    @ApiModelProperty(value = "Return is pending records", dataType="java.lang.boolean", required = true)
    @NotNull
    private Boolean pending;

    @ApiModelProperty(value = "Return is pending records", dataType="java.lang.boolean")
    private Boolean claimer;

    public LocalDateTime getStartDateAsLocalDateTime() {
        if(startDate == null) {
            return null;
        }

        return LocalDateTime.from(ISO_LOCAL_DATE_TIME.parse(startDate));
    }

    public LocalDateTime getEndDateAsLocalDateTime() {
        if(endDate == null) {
            return null;
        }

        return LocalDateTime.from(ISO_LOCAL_DATE_TIME.parse(endDate));
    }

    public Claim toDomain() {
        return Claim.builder()
                .ispb(ispb)
                .personType(personType)
                .cpfCnpj(cpfCnpj)
                .branchNumber(branchNumber)
                .accountNumber(accountNumber)
                .accountType(accountType)
                .build();
    }

}