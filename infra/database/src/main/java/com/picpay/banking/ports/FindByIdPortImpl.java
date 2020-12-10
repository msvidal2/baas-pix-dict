package com.picpay.banking.ports;

import com.picpay.banking.entity.ClaimEntity;
import com.picpay.banking.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.FindByIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindByIdPortImpl implements FindByIdPort {

    private final ClaimRepository claimRepository;

    @Override
    public Optional<Claim> find(String id) {
        return claimRepository.findById(id)
                .map(ClaimEntity::toClaim);
    }

}
