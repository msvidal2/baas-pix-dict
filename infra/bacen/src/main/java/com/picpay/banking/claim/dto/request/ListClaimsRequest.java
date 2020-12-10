package com.picpay.banking.claim.dto.request;

import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.pix.core.domain.Claim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ListClaimsRequest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final String participant;
    private final Boolean isDonor;
    private final Boolean isClaimer;
    private final List<ClaimStatus> status;
    private final ClaimType type;
    private final String modifiedAfter;
    private final String modifiedBefore;
    private final Integer limit;

    public static ListClaimsRequest from(Claim claim, Integer limit, LocalDateTime startDate, LocalDateTime endDate) {
        return ListClaimsRequest.builder()
                .limit(limit)
                .isClaimer(claim.getIsClaim())
                .isDonor(claim.getIsClaim() != null ?  !claim.getIsClaim() : null)
                .modifiedAfter(DATE_FORMATTER.format(startDate))
                .modifiedBefore(DATE_FORMATTER.format(endDate))
                .participant(String.valueOf(claim.getIspb()))
                .status(claim.getClaimSituation() != null ? List.of(ClaimStatus.resolve(claim.getClaimSituation())) : null)
                .type(claim.getClaimType() != null ? ClaimType.resolve(claim.getClaimType()) : null)
                .build();
    }

    public static ListClaimsRequest from(Claim claim, Integer limit) {
        return ListClaimsRequest.builder()
                .limit(limit)
                .participant(String.valueOf(claim.getIspb()))
                .status(List.of(ClaimStatus.WAITING_RESOLUTION))
                .type(ClaimType.resolve(claim.getClaimType()))
                .modifiedAfter(DATE_FORMATTER.format(claim.getStarDate()))
                .modifiedBefore(DATE_FORMATTER.format(claim.getEndDate()))
                .build();
    }
}
