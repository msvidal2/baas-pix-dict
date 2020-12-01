package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.FindByIdPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Luis Silva
 * @version 1.0 01/12/2020
 */
@Component
public class FindByIdPortImpl implements FindByIdPort {

    @Override
    public Optional<Claim> find(final String id) {
        return Optional.empty();
    }

}
