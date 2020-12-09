package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompleteClaimPortImpl implements CompleteClaimPort {

    private final ClaimRepository claimRepository;

    @Override
    public Claim complete(Claim claim, String requestIdentifier) {
        ClaimEntity entity = claimRepository.findClaimerClaimById(claim.getClaimId(), claim.getIspb());
        entity.setStatus(ClaimSituation.COMPLETED);
        claimRepository.save(entity);

        return entity.toClaim();
    }
}
