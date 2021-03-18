package com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountPixKeyRequestWebDTO {

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NotNull
    private KeyType type;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NotNull
    private int ispb;

    @ApiModelProperty(value = "Branch number of client", dataType="java.lang.String", required = true)
    @NotNull
    private String branchNumber;

    @ApiModelProperty(value = "Account type of client", required = true)
    @NotNull
    private AccountType accountType;

    @ApiModelProperty(value = "Account number of client", dataType="java.lang.String", required = true)
    @NotNull
    private String accountNumber;

    @ApiModelProperty(value = "Account opening date. Format: aaaa-mm-ddTHH:mm:ss.sssZ", required = true)
    @NotNull
    private LocalDateTime accountOpeningDate;

    @ApiModelProperty(value = "Reason for update", required = true)
    @NotNull
    private UpdateReasonDTO reason;


    @ApiModelProperty(value =
            "CPF ou algum outro identificador do usuário final que originou a consulta de chave. Será utilizado pelo"
                    +" DICT para calcular o rate-limiting.", required = true)
    @NotNull
    private String userId;

    public PixKeyEventData toEventData(final String key, final Reason reason) {
        return PixKeyEventData.builder()
                .key(key)
                .type(type)
                .ispb(ispb)
                .branchNumber(branchNumber)
                .accountType(accountType)
                .accountNumber(accountNumber)
                .accountOpeningDate(accountOpeningDate)
                .reason(reason)
                .build();
    }

}
