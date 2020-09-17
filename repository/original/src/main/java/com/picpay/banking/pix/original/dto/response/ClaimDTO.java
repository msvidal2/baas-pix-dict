package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {

    private Object claimer;
    private ClaimerAccountDTO claimerAccount;
    private String completionPeriodEnd;
    private String donorParticipant;
    private String id;
    private String key;
    private KeyTypeOriginal keyType;
    private String lastModified;
    private String resolutionPeriodEnd;
    private String status;
    private String type;

    public Claim toDomain() {
        return Claim.builder()
                .claimId(id)
//                .claimSituation(status) // TODO: aguardando swagger para fazer o de para
//                .claimType(type)  // TODO: aguardando swagger para fazer o de para
                .key(key)
                .keyType(keyType.getKeyType())
                .ispb(claimerAccount.getParticipant())
                .branchNumber(claimerAccount.getBranch())
                .accountType(claimerAccount.getAccountType().getAccountTypeDomain())
                .accountNumber(String.valueOf(claimerAccount.getAccountNumber()))
//                .donorIspb(Integer.parseInt(donorParticipant)) // TODO: indefinido o retorno
//                .resolutionThresholdDate(resolutionPeriodEnd) // TODO: aguardando swagger para maiores informações
//                .completionThresholdDate(completionPeriodEnd)  // TODO: aguardando swagger para maiores informações
//                .lastModifiedDate(lastModified) // TODO: aguardando swagger para maiores informações
                .build();
    }
}
