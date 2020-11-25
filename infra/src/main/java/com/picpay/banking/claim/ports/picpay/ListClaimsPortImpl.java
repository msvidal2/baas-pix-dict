package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
public class ListClaimsPortImpl implements ListClaimPort {

    private final ClaimRepository claimRepository;

    @Value("${picpay.ispb}")
    private final String picPayParticipantNumber;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer, Boolean isDonor, LocalDateTime startDate, LocalDateTime endDate, String requestIdentifier) {
        List<ClaimEntity> claimEntityList = new ArrayList<>();

        if(isNull(endDate))
            endDate = LocalDateTime.now(ZoneId.of("UTC"));

        // limit++ -> pega um registro a mais do que o definido no limite. Se o size da lista retornada for maior que o limit, eh porque hasNext.
        // alternativa -> fazer uma consulta de count antes. Se o count for maior que o limit, eh porque hasNext. Desvantagem: duas consultas na base.

        if(nonNull(isClaimer)) {
            claimEntityList = claimRepository.findAllClaimsWhereIsClaimerAndHasDates(limit++, Integer.valueOf(picPayParticipantNumber), startDate, endDate);
        } else if(nonNull(isDonor)) {
            claimEntityList = claimRepository.findAllClaimsWhereIsDonorAndHasDates(limit++, Integer.valueOf(picPayParticipantNumber), startDate, endDate);
        }

        return toClaimIterable(claimEntityList, limit);
    }

    private ClaimIterable toClaimIterable(List<ClaimEntity> claimEntityList, Integer limit){
        return ClaimIterable.builder()
                .hasNext(claimEntityList.size() > limit)
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
