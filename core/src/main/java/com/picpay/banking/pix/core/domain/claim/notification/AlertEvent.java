package com.picpay.banking.pix.core.domain.claim.notification;

import com.picpay.banking.pix.core.domain.AccountType;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AlertEvent {

    private String idAlert;
    private String emitterSystem;

    private Integer ispb;
    private AlertPersonType personTypeReceiver;
    private String taxIdReceiver;
    private String nameReceiver;
    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;

    private String alertType;
    private AlertCategory category;

    private String subject;
    private String textAlert;
    private Integer priority;

}
