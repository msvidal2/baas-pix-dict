package com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request;

import com.picpay.banking.pix.core.domain.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountPixKeyRequestWebDTO {

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NonNull
    private KeyType type;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NonNull
    private int ispb;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String", required = true)
    @NonNull
    private String branchNumber;

    @ApiModelProperty(value = "Account type of client", required = true)
    @NonNull
    private AccountType accountType;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String", required = true)
    @NonNull
    private String accountNumber;

    @ApiModelProperty(value = "Account opening date. Format: aaaa-mm-ddTHH:mm:ss.sssZ", required = true)
    @NonNull
    private LocalDateTime accountOpeningDate;

    @ApiModelProperty(value = "Reason for update", required = true)
    @NonNull
    private UpdateReasonDTO reason;

    @ApiModelProperty(value =
            "CPF ou algum outro identificador do usuário final que originou a consulta de chave. Será utilizado pelo"
                    +" DICT para calcular o rate-limiting.", required = true)
    @NonNull
    private String userId;

    public PixKey toDomain(String key) {
        return PixKey.builder()
                .key(key)
                .type(type)
                .ispb(ispb)
                .branchNumber(branchNumber)
                .accountType(accountType)
                .accountNumber(accountNumber)
                .accountOpeningDate(accountOpeningDate)
                .situation(PixKeySituation.ACTIVE)
                .build();
    }

}
