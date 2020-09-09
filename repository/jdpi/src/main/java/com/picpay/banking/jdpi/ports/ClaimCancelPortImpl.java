package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.ClaimCancelRequestDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.ClaimCancelPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ClaimCancelPortImpl implements ClaimCancelPort {

    private ClaimJDClient claimJDClient;

    @Override
    public Claim cancel(Claim claim, boolean canceledClaimant, ClaimCancelReason reason, String requestIdentifier) {
        var request = ClaimCancelRequestDTO.builder()
                .claimId(claim.getClaimId())
                .ispb(claim.getIspb())
                .canceledClaimant(canceledClaimant)
                .reason(reason.getValue())
                .build();

        var response = claimJDClient.cancel(requestIdentifier, claim.getClaimId(), request);

        return Claim.builder()
                .claimId(response.getClaimId())
                .claimSituation(ClaimSituation.resolve(response.getClaimSituation()))
                .resolutionThresholdDate(response.getResolutionThresholdDate())
                .completionThresholdDate(response.getCompletionThresholdDate())
                .lastModifiedDate(response.getLastModifiedDate())
                .build();
    }

}
