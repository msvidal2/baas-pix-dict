package com.picpay.banking.infraction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 27/11/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CancelInfractionReportRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CancelInfractionReportRequest {

    @XmlElement(name = "InfractionReportId")
    private String infractionReportId;

    @XmlElement(name = "Participant")
    private String participant;

    public static CancelInfractionReportRequest from(String infractionReportId, Integer participant) {
        return CancelInfractionReportRequest.builder()
                .infractionReportId(infractionReportId)
                .participant(Integer.toString(participant))
                .build();
    }

}
