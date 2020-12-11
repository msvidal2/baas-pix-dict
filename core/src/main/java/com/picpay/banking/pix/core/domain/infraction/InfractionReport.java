package com.picpay.banking.pix.core.domain.infraction;

import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.EnumSet;

import static com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation.ACKNOWLEDGED;
import static com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation.ANALYZED;
import static com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation.OPEN;
import static com.picpay.banking.pix.core.exception.InfractionReportError.INFRACTION_REPORT_ALREADY_OPEN;

@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InfractionReport implements Serializable {

    private static final EnumSet<InfractionReportSituation> OPEN_STATES = EnumSet.of(OPEN, ANALYZED, ACKNOWLEDGED);

    private String infractionReportId;
    @EqualsAndHashCode.Include
    private InfractionType infractionType;
    @EqualsAndHashCode.Include
    private String endToEndId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private String ispbDebited;
    private String ispbCredited;
    private LocalDateTime dateCreate;
    @Setter
    private LocalDateTime dateLastUpdate;
    @EqualsAndHashCode.Include
    private String details;
    @Setter
    private InfractionAnalyze analyze;

    public void validateSituation() {
        if (OPEN_STATES.contains(this.getSituation()))
            throw new InfractionReportException(INFRACTION_REPORT_ALREADY_OPEN);
    }

}
