package com.picpay.banking.claim.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.pix.core.domain.Claim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ListClaimsRequest {

    private final String participant;
    private final Boolean isDonor;
    private final Boolean isClaimer;
    private final List<ClaimStatus> status;
    private final ClaimType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime modifiedAfter;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime modifiedBefore;
    private final Integer limit;

    public static ListClaimsRequest from(Claim claim, Integer limit, Boolean isClaimer, Boolean isDonor, LocalDateTime startDate, LocalDateTime endDate) {
        return ListClaimsRequest.builder()
                .limit(limit)
                .isClaimer(isClaimer)
                .isDonor(isDonor)
                .modifiedBefore(startDate)
                .modifiedAfter(endDate)
                .participant(String.valueOf(claim.getIspb()))
                .status(List.of(ClaimStatus.resolve(claim.getClaimSituation())))
                .type(ClaimType.resolve(claim.getClaimType()))
                .build();
    }
}
