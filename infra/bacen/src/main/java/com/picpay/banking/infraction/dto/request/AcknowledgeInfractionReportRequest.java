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
 * @version 1.0 09/12/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "AcknowledgeInfractionReportRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcknowledgeInfractionReportRequest {

    @XmlElement(name = "InfractionReportId")
    private String infractionReportId;

    @XmlElement(name = "Participant")
    private String participant;

    public static AcknowledgeInfractionReportRequest from(String infractionReportId, String ispb) {
        return AcknowledgeInfractionReportRequest.builder()
                .infractionReportId(infractionReportId)
                .participant(ispb)
                .build();
    }

}
