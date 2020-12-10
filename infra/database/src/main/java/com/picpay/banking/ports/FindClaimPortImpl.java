package com.picpay.banking.ports;

import com.picpay.banking.entity.ClaimEntity;
import com.picpay.banking.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindClaimPortImpl implements FindClaimPort {

    private final ClaimRepository claimRepository;

    @Override
    public Optional<Claim> findClaim(String claimId, Integer ispb, boolean claimant) {
        if (claimant) {
            return getClaim(claimRepository.findClaimerClaimById(claimId, ispb));
        }

        return getClaim(claimRepository.findDonorClaimById(claimId, ispb));
    }

    private Optional<Claim> getClaim(ClaimEntity claimEntity) {
        return Optional.ofNullable(claimEntity)
                .map(ClaimEntity::toClaim);
    }

}
