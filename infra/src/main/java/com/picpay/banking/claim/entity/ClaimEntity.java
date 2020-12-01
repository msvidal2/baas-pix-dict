package com.picpay.banking.claim.entity;

import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
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
    private ClaimStatus status;

    private LocalDateTime completionPeriodEnd;

    private LocalDateTime resolutionPeriodEnd;

    private LocalDateTime lastModified;

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    private ClaimConfirmationReason confirmReason;

    private ClaimCancelReason cancelReason;

    private boolean cancelledByClaimant;

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
                .status(ClaimStatus.resolve(claim.getClaimSituation()))
                .completionPeriodEnd(claim.getCompletionThresholdDate())
                .resolutionPeriodEnd(claim.getResolutionThresholdDate())
                .lastModified(claim.getLastModifiedDate())
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
                .donorIspb(claimerParticipant)
                .branchNumber(claimerBranch)
                .accountNumber(claimerAccountNumber)
                .accountType(claimerAccountType)
                .accountOpeningDate(claimerAccountOpeningDate)
                .personType(claimerType)
                .cpfCnpj(claimerTaxId)
                .name(claimerName)
                .donorIspb(donorParticipant)
                .claimSituation(status.getClaimSituation())
                .completionThresholdDate(completionPeriodEnd)
                .resolutionThresholdDate(resolutionPeriodEnd)
                .lastModifiedDate(lastModified)
                .confirmationReason(confirmReason)
                .cancelReason(cancelReason)
                .isClaim(cancelledByClaimant)
                .correlationId(correlationId)
                .build();
    }

}
