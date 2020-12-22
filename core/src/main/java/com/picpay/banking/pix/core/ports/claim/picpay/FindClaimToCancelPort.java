package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;

import java.time.LocalDateTime;
import java.util.List;

public interface FindClaimToCancelPort {

    List<Claim> find(ClaimType type, List<ClaimSituation> status, Integer ispb, LocalDateTime resolutionPeriodEnd, Integer limit);

}