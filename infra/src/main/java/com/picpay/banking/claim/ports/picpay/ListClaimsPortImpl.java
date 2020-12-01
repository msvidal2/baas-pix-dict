package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListClaimsPortImpl implements ListClaimPort {

    private final ClaimRepository claimRepository;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer, Boolean isDonor, LocalDateTime startDate, LocalDateTime endDate, String requestIdentifier) {
        Page<ClaimEntity> claimEntityList = null;

        if(nonNull(isClaimer)) {
            claimEntityList = claimRepository.findAllClaimsWhereIsClaimer(claim.getIspb(), startDate, endDate, PageRequest.of(0, limit));
        } else if(nonNull(isDonor)) {
            claimEntityList = claimRepository.findAllClaimsWhereIsDonor(claim.getIspb(), startDate, endDate, PageRequest.of(0, limit));
        }

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
