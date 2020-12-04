package com.picpay.banking.claim.ports.bacen;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.ListClaimsRequest;
import com.picpay.banking.claim.dto.response.ListClaimsResponse;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListClaimsBacenPortImpl implements ListClaimsBacenPort {

    private final BacenClaimClient bacenClaimClient;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer, LocalDateTime startDate, LocalDateTime endDate) {
        ListClaimsRequest request = ListClaimsRequest.from(claim, limit, startDate, endDate);

        ListClaimsResponse response = bacenClaimClient.listClaims(request);

        return response.toClaimIterable();
    }
}
