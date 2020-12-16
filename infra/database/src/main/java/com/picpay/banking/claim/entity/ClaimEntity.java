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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimType type;

    @Column(nullable = false,name = "pix_key")
    private String key;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @Column(nullable = false)
    private Integer claimerParticipant;

    private String claimerBranch;

    @Column(nullable = false)
    private String claimerAccountNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType claimerAccountType;

    @Column(nullable = false)
    private LocalDateTime claimerAccountOpeningDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonType claimerType;

    @Column(nullable = false)
    private String claimerTaxId;

    @Column(nullable = false)
    private String claimerName;

    private Integer donorParticipant;

    @Enumerated(EnumType.STRING)
    private ClaimSituation status;

    private LocalDateTime completionPeriodEnd;

    private LocalDateTime resolutionPeriodEnd;

    private LocalDateTime lastModified;

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    private ClaimConfirmationReason confirmReason;

    private ClaimCancelReason cancelReason;

    private Boolean cancelledByClaimant;

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
