package com.picpay.banking.pix.original.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.original.dto.ReasonOriginal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveAccessKeyDTO {

    private String key;
    private ReasonOriginal reason;

    public static RemoveAccessKeyDTO from(PixKey pixKey, RemoveReason reason) {
        return RemoveAccessKeyDTO.builder()
                .key(pixKey.getKey())
                .reason(ReasonOriginal.resolve(reason.getValue()))
                .build();
    }

}
