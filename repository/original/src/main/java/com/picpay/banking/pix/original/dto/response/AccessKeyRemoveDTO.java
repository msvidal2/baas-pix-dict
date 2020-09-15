package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessKeyRemoveDTO {

    private String key;
    private Integer returnCode;
    private String returnMessage;

    public PixKey toDomain() {
        return PixKey.builder()
                .key(key)
                .build();
    }

}
