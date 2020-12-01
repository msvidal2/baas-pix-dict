package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;

import java.util.Optional;

public interface FindByIdPort {

    Optional<Claim> find(String id);

}
