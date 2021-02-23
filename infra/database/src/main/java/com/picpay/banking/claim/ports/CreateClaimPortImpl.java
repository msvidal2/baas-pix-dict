package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateClaimPortImpl implements CreateClaimPort {

    private final ClaimRepository claimRepository;

    @Override
    public Claim saveClaim(Claim claim, String requestIdentifier) {
        ClaimEntity entity = ClaimEntity.from(claim);
        ClaimEntity savedEntity = claimRepository.save(entity);

        return savedEntity.toClaim();
    }

}
