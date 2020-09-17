package com.picpay.banking.pix.original.dto.request;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.original.dto.ClaimReasonOriginal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimConfirmationRequestDTO {

    private String claimId;
    private String reason;
    private String signature;

    public static ClaimConfirmationRequestDTO from(Claim claim, ClaimConfirmationReason reason) {
        return ClaimConfirmationRequestDTO.builder()
                .claimId(claim.getClaimId())
                .reason(ClaimReasonOriginal.from(reason.getValue()).name())
                .signature("string") // TODO: verificar qual informação deve ser enviada
                .build();
    }
}
