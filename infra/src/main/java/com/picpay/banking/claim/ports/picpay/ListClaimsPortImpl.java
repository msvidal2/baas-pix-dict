package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListClaimsPortImpl implements ListClaimPort {

    private final ClaimRepository claimRepository;

    @Value("${picpay.ispb}")
    private String picPayParticipantNumber;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer, Boolean isDonor, LocalDateTime startDate, LocalDateTime endDate, String requestIdentifier) {
        List<ClaimEntity> claimEntityList = new ArrayList<>();
        long findSize = 0;
        Pageable pageable = PageRequest.of(0, limit);

        if(nonNull(isClaimer)) {
            claimEntityList = claimRepository.findAllClaimsWhereIsClaimer(Integer.valueOf(picPayParticipantNumber), startDate, endDate, pageable);
            findSize = claimRepository.countAllClaimsWhereIsClaimer(Integer.valueOf(picPayParticipantNumber), startDate, endDate);
        } else if(nonNull(isDonor)) {
            claimEntityList = claimRepository.findAllClaimsWhereIsDonor(Integer.valueOf(picPayParticipantNumber), startDate, endDate, pageable);
            findSize = claimRepository.countAllClaimsWhereIsDonor(Integer.valueOf(picPayParticipantNumber), startDate, endDate);
        }

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
