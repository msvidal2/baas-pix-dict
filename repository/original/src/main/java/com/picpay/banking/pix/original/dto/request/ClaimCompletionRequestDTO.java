package com.picpay.banking.pix.original.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimCompletionRequestDTO {

    private String claimId;
    private String reason;
    private String requestId;
    private String signature;

}
