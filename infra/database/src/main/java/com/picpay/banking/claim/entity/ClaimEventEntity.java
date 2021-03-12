package com.picpay.banking.claim.entity;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimEventType type;

    @JoinColumn(name = "claim_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimEntity claim;

    @Type(type = "json")
    @Column(name = "event_data", columnDefinition = "json", nullable = false)
    private ClaimEntity data;

    @CreatedDate
    private LocalDateTime creationDate;

    public static ClaimEventEntity of(Claim claim, ClaimEventType eventType) {
        var claimEntity = ClaimEntity.from(claim);
        return ClaimEventEntity.builder()
                .claim(claimEntity)
                .data(claimEntity)
                .type(eventType)
                .build();
    }

}
