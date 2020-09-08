package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClaimIterable {

    private Boolean hasNext;
    private Integer count;
    private List<Claim> claims;


}
