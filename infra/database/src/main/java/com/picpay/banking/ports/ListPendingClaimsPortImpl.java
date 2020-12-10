package com.picpay.banking.ports;

import com.picpay.banking.entity.ClaimEntity;
import com.picpay.banking.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.picpay.ListPendingClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListPendingClaimsPortImpl implements ListPendingClaimPort {

    private final ClaimRepository claimRepository;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, String requestIdentifier) {
        Page<ClaimEntity> claimEntityList = claimRepository.findAllPendingClaims(claim.getIspb(),
                ClaimSituation.getPending(), PageRequest.of(0, limit));

        return toClaimIterable(claimEntityList);
    }

    private ClaimIterable toClaimIterable(Page<ClaimEntity> claimEntityList){
        return ClaimIterable.builder()
                .hasNext(claimEntityList.hasNext())
                .count(claimEntityList.getNumberOfElements())
                .claims(claimEntityList.map(ClaimEntity::toClaim).toList())
                .build();
    }
}
