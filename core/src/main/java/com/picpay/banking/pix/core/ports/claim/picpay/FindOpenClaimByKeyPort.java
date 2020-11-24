package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;

import java.util.Optional;

public interface FindOpenClaimByKeyPort {

    Optional<Claim> find(String key);

}