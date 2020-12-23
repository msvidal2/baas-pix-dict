package com.picpay.banking.claim.entity;

import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "claim")
public class ClaimEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private ClaimType type;

    @Column(nullable = false, name = "pix_key")
    private String key;

    @Column(nullable = false, name = "key_type")
    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @Column(nullable = false, name = "claimer_participant")
    private Integer claimerParticipant;

    @Column(name = "claimer_branch")
    private String claimerBranch;

    @Column(nullable = false, name = "claimer_account_number")
    private String claimerAccountNumber;

    @Column(nullable = false, name = "claimer_account_type")
    @Enumerated(EnumType.STRING)
    private AccountType claimerAccountType;

    @Column(nullable = false, name = "claimer_account_opening_date")
    private LocalDateTime claimerAccountOpeningDate;

    @Column(nullable = false, name = "claimer_type")
    @Enumerated(EnumType.STRING)
    private PersonType claimerType;

    @Column(nullable = false, name = "claimer_tax_id")
    private String claimerTaxId;

    @Column(nullable = false, name = "claimer_name")
    private String claimerName;

    @Column(name = "donor_participant")
    private Integer donorParticipant;

    @Enumerated(EnumType.STRING)
    private ClaimSituation status;

    @Column(name = "completion_period_end")
    private LocalDateTime completionPeriodEnd;

    @Column(name = "resolution_period_end")
    private LocalDateTime resolutionPeriodEnd;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "creation_date")
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    @LastModifiedDate
    private LocalDateTime updateDate;

    @Column(name = "confirm_reason")
    @Enumerated(EnumType.STRING)
    private ClaimConfirmationReason confirmReason;

    @Column(name = "cancel_reason")
    @Enumerated(EnumType.STRING)
    private ClaimCancelReason cancelReason;

    @Column(name = "cancelled_by_claimant")
    private Boolean cancelledByClaimant;

    @Column(name = "correlation_id")
    private String correlationId;

    public static ClaimEntity from(Claim claim) {
        return ClaimEntity.builder()
                .id(claim.getClaimId())
                .type(claim.getClaimType())
                .key(claim.getKey())
                .keyType(claim.getKeyType())
                .claimerAccountNumber(claim.getAccountNumber())
                .claimerAccountOpeningDate(claim.getAccountOpeningDate())
                .claimerAccountType(claim.getAccountType())
                .claimerBranch(claim.getBranchNumber())
                .claimerName(claim.getOwnerName())
                .claimerParticipant(claim.getDonorIspb())
                .claimerTaxId(claim.getCpfCnpj())
                .claimerType(claim.getPersonType())
                .donorParticipant(claim.getDonorIspb())
                .status(claim.getClaimSituation())
                .completionPeriodEnd(claim.getCompletionThresholdDate())
                .resolutionPeriodEnd(claim.getResolutionThresholdDate())
                .lastModified(Optional.ofNullable(claim.getLastModifiedDate()).orElse(null))
                .confirmReason(claim.getConfirmationReason())
                .cancelReason(claim.getCancelReason())
                .cancelledByClaimant(claim.getIsClaim())
                .correlationId(claim.getCorrelationId())
                .build();
    }

    public Claim toClaim() {
        return Claim.builder()
                .claimId(id)
                .claimType(type)
                .key(key)
                .keyType(keyType)
                .donorIspb(donorParticipant)
                .branchNumber(claimerBranch)
                .accountNumber(claimerAccountNumber)
                .accountType(claimerAccountType)
                .accountOpeningDate(claimerAccountOpeningDate)
                .personType(claimerType)
                .cpfCnpj(claimerTaxId)
                .name(claimerName)
                .claimSituation(status)
                .completionThresholdDate(completionPeriodEnd)
                .resolutionThresholdDate(resolutionPeriodEnd)
                .lastModifiedDate(lastModified)
                .ispb(claimerParticipant)
                .confirmationReason(confirmReason)
                .cancelReason(cancelReason)
                .isClaim(cancelledByClaimant)
                .correlationId(correlationId)
                .build();
    }

}