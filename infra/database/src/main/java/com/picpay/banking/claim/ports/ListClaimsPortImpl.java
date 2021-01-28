package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.claim.repository.specifications.ListClaims;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListClaimsPortImpl implements ListClaimPort {

    private final ClaimRepository claimRepository;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer, Boolean isPending, LocalDateTime startDate, LocalDateTime endDate) {

        var claims = claimRepository.findAll(
                new ListClaims(claim, isClaimer, isPending, startDate, endDate),
                PageRequest.of(0, limit));

        return toClaimIterable(claims);
    }

    private ClaimIterable toClaimIterable(Page<ClaimEntity> claimEntityList) {
        return ClaimIterable.builder()
                .hasNext(claimEntityList.hasNext())
                .count(claimEntityList.getNumberOfElements())
                .claims(claimEntityList.map(ClaimEntity::toClaim).toList())
                .build();
    }

}
