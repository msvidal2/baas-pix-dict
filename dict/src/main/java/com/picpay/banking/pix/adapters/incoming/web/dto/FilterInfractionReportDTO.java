package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterInfractionReportDTO {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.Integer", required = true)
    @NotNull
    private Integer ispb;

    @ApiModelProperty(value = "Situation of Infraction", dataType="java.lang.String", required = true)
    private InfractionReportSituation situation;

    @ApiModelProperty(value = "Start date: ISO Format (yyyy-MM-dd'T'HH:mm:ss.SSS)")
    @NotNull
    private String startDate;

    @ApiModelProperty(value = "End date: ISO Format (yyyy-MM-dd'T'HH:mm:ss.SSS)")
    private String endDate;

    public LocalDateTime getStartDateAsLocalDateTime() {
        if(startDate == null) {
            return null;
        }
        return LocalDateTime.from(dateTimeFormatter.parse(startDate));
    }

    public LocalDateTime getEndDateAsLocalDateTime() {
        if(endDate == null) {
            return null;
        }
        return LocalDateTime.from(dateTimeFormatter.parse(endDate));
    }

}
