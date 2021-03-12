package com.picpay.banking.claim.entity;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "claim_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClaimEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimEventType type;

    @Column(nullable = false)
    private String requestIdentifier;

    private String claimId;

    @Type(type = "json")
    @Column(name = "event_data", columnDefinition = "json", nullable = false)
    private Claim data;

    @CreatedDate
    private LocalDateTime creationDate;

    public static ClaimEventEntity of(String requestIdentifier, Claim claim, ClaimEventType eventType) {
        return ClaimEventEntity.builder()
                .type(eventType)
                .requestIdentifier(requestIdentifier)
                .claimId(claim.getClaimId())
                .data(claim)
                .creationDate(LocalDateTime.now())
                .build();
    }

}
