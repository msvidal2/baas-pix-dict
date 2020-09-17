package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.Claim;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimResponseDTO {

    private ClaimDTO claim;
    private String signature;

}
