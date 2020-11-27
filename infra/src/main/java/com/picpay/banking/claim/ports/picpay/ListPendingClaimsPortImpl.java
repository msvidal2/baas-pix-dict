package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListPendingClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListPendingClaimsPortImpl implements ListPendingClaimPort {

    private final ClaimRepository claimRepository;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, String requestIdentifier) {
        List<ClaimEntity> claimEntityList = claimRepository.findAllPendingClaims(ClaimStatus.getPending(), PageRequest.of(0, limit));
        long findSize = claimRepository.countAllPendingClaims(ClaimStatus.getPending());

        return toClaimIterable(claimEntityList, limit, findSize);
    }

    private ClaimIterable toClaimIterable(List<ClaimEntity> claimEntityList, Integer limit, long findSize){
        return ClaimIterable.builder()
                .hasNext(findSize > limit)
                .count(claimEntityList.size())
                .claims(getClaims(claimEntityList))
                .build();
    }

    private List<Claim> getClaims(List<ClaimEntity> claimEntityList){
        List<Claim> claims = new ArrayList<>();
        claimEntityList.forEach(c -> claims.add(c.toClaim()));
        return claims;
    }
}
