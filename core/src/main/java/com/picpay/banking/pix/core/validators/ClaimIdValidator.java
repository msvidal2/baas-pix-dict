package com.picpay.banking.pix.core.validators;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClaimIdValidator {

    public static void validate(String claimId) {
        if(StringUtils.isBlank(claimId)) {
            throw new IllegalArgumentException("Claim id cannot be null");
        }

        var pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", Pattern.CASE_INSENSITIVE);
        var matcher = pattern.matcher(claimId);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Invalid claim id: "+ claimId);
        }
    }

}
