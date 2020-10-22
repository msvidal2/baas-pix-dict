package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

import java.util.regex.Pattern;

public class ClaimIdItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim claim) {
        if(claim.getClaimId() == null) {
            throw new IllegalArgumentException("The claim id cannot be null");
        }

        var pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", Pattern.CASE_INSENSITIVE);
        var matcher = pattern.matcher(claim.getClaimId());
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Invalid claim id: "+ claim.getClaimId());
        }
    }

}
