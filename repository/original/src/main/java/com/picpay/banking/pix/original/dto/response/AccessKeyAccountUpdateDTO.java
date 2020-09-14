package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessKeyAccountUpdateDTO {

    private LocalDateTime creationDate;
    private LocalDateTime keyOwnershipDate;
    private Integer returnCode;
    private String returnMessage;

    public PixKey toDomain() {
        return PixKey.builder()
                .createdAt(creationDate)
                .startPossessionAt(keyOwnershipDate)
                .build();
    }

}
