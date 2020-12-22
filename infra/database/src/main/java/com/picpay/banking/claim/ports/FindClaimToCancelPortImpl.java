package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindClaimToCancelPortImpl implements FindClaimToCancelPort {

    private final ClaimRepository claimRepository;

    @Override
    public List<Claim> find(ClaimType type, List<ClaimSituation> status, Integer ispb, LocalDateTime resolutionPeriodEnd, Integer limit) {
        // TODO reverter gambiarra
        resolutionPeriodEnd = LocalDateTime.now().plusDays(10);
        type = ClaimType.POSSESSION_CLAIM;
        Page<ClaimEntity> claimEntityList = claimRepository.findClaimToCancel(type, List.of(ClaimSituation.OPEN), ispb,
                resolutionPeriodEnd, PageRequest.of(0, limit));

        return claimEntityList.map(ClaimEntity::toClaim).toList();
    }
}