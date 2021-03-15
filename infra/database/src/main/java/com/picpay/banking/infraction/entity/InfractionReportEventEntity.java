package com.picpay.banking.infraction.entity;

import com.picpay.banking.pix.core.domain.InfractionReportEvent;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.events.InfractionReportEventData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "infraction_report_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InfractionReportEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InfractionReportEvent type;

    private String infractionReportId;

    @Type(type = "json")
    @Column(name = "event_data", columnDefinition = "json", nullable = false)
    private InfractionReportEventData data;

    @CreatedDate
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String requestIdentifier;

}
