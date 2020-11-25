package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.picpay.banking.claim.dto.response.ClaimStatus.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindOpenClaimByKeyPortImpl implements FindOpenClaimByKeyPort {

    private final ClaimRepository claimRepository;

    @Override
    public Optional<Claim> find(String key) {

        ClaimEntity claimEntity = claimRepository.findOpenClaimByKey(key, List.of(OPEN, WAITING_RESOLUTION, CONFIRMED));

        return Optional.ofNullable(claimEntity)
                .map(ClaimEntity::toClaim);
    }

}
