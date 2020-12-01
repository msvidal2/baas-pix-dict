package com.picpay.banking.pix.core.validators.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.validators.ClaimIdValidator;
import com.picpay.banking.pix.core.validators.IspbValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfirmClaimValidator {

    public static void validate(Claim claim, ClaimConfirmationReason reason, String requestIdentifier) {
        if (StringUtils.isBlank(requestIdentifier)) {
            throw new IllegalArgumentException("Request identifier cannot be empty");
        }

        ClaimIdValidator.validate(claim.getClaimId());
        IspbValidator.validate(claim.getIspb());

        if (Objects.isNull(reason)) {
            throw new IllegalArgumentException("Reason cannot be null");
        }
    }

}
