package com.picpay.banking.pix.core.validators.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import lombok.AllArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;

@AllArgsConstructor
public class WorkInfractionValidator {

    public static void validate(final InfractionReport infractionReport) {

        if (ObjectUtils.isEmpty(infractionReport)) {
            throw new IllegalArgumentException("InfractionReport cannot be null");
        }

        if (ObjectUtils.isEmpty(infractionReport.getInfractionReportId())) {
            throw new IllegalArgumentException("InfractionReportId cannot be empty or null");
        }

        if (!String.valueOf(infractionReport.getIspbRequester()).matches("^[0-9]{8}$")) {
            throw new IllegalArgumentException("The ISPB must contain 8 digits");
        }

        if (ObjectUtils.isEmpty(infractionReport.getSituation())) {
            throw new IllegalArgumentException("Situation cannot be empty or null");
        }

    }

}
