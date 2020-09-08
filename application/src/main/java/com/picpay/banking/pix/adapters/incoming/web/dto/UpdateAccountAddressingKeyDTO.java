package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.UpdateReason;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAccountAddressingKeyDTO {

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
    private UpdateReason reason;

    @ApiModelProperty(value = "We suggest using UUID (v4) typing, 36 characters long.", required = true)
    @NonNull
    private String requestIdentifier;

    @ApiModelProperty(value =
            "CPF ou algum outro identificador do usuário final que originou a consulta de chave. Será utilizado pelo"
                    +" DICT para calcular o rate-limiting.", required = true)
    @NonNull
    private String userId;
}
