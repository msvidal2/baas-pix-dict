package com.picpay.banking.claim.dto.request;

import com.picpay.banking.pixkey.dto.request.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Claimer {

    private final OwnerType type;
    private final String taxIdNumber;
    private final String name;

}
