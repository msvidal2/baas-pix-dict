package com.picpay.banking.claim.entity;

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
public class ClaimEvent {

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

}
