package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateEntryRequest {

    private Entry entry;
    private Reason reason;
    private String requestId;

    public static CreateEntryRequest from(PixKey pixKey, CreateReason reason, String requestId) {
        return CreateEntryRequest.builder()
                .entry(Entry.from(pixKey))
                .reason(Reason.resolve(reason))
                .requestId(requestId)
                .build();
    }

}
