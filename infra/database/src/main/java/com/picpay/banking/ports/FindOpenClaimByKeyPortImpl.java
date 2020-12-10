package com.picpay.banking.ports;

import com.picpay.banking.entity.ClaimEntity;
import com.picpay.banking.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindOpenClaimByKeyPortImpl implements FindOpenClaimByKeyPort {

    private final ClaimRepository claimRepository;

    @Override
    public Optional<Claim> find(String key) {

        ClaimEntity claimEntity = claimRepository.findOpenClaimByKey(key, ClaimSituation.getPending());

        return Optional.ofNullable(claimEntity)
                .map(ClaimEntity::toClaim);
    }


}
