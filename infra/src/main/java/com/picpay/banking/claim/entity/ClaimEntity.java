package com.picpay.banking.claim.entity;

import com.picpay.banking.claim.dto.request.ClaimType;
import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pixkey.dto.request.AccountType;
import com.picpay.banking.pixkey.dto.request.KeyType;
import com.picpay.banking.pixkey.dto.request.OwnerType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "claim")
@Data
@Builder
@Entity
public class ClaimEntity {

    @Id
    private String id;
    private ClaimType type;
    private String key;
    private KeyType keyType;
    private String claimerParticipant;
    private String claimerBranch;
    private String claimerAccountNumber;
    private AccountType claimerAccountType;
    private LocalDateTime claimerAccountOpeningDate;
    private OwnerType claimerType;
    private String claimerTaxId;
    private String claimerName;
    private String donorParticipant;
    private ClaimStatus status;
    private LocalDateTime completionPeriodEnd;
    private LocalDateTime resolutionPeriodEnd;
    private LocalDateTime lastModified;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime updateDate;

    public static ClaimEntity from(Claim claim) {
        return ClaimEntity.builder()
                .id(claim.getClaimId())
                .type(ClaimType.resolve(claim.getClaimType()))
                .key(claim.getKey())
                .keyType(KeyType.resolve(claim.getKeyType()))
                .claimerAccountNumber(claim.getAccountNumber())
                .claimerAccountOpeningDate(claim.getAccountOpeningDate())
                .claimerAccountType(AccountType.resolve(claim.getAccountType()))
                .claimerBranch(claim.getBranchNumber())
                .claimerName(claim.getOwnerName())
                .claimerParticipant(String.valueOf(claim.getDonorIspb()))
                .claimerTaxId(claim.getCpfCnpj())
                .claimerType(OwnerType.resolve(claim.getPersonType()))
                .donorParticipant(String.valueOf(claim.getDonorIspb()))
                .status(ClaimStatus.resolve(claim.getClaimSituation()))
                .completionPeriodEnd(claim.getCompletionThresholdDate())
                .resolutionPeriodEnd(claim.getResolutionThresholdDate())
                .lastModified(claim.getLastModifiedDate())
                .build();
    }

    public Claim toClaim() {
        return Claim.builder()
                .claimId(id)
                .claimType(type.getClaimType())
                .key(key)
                .keyType(keyType.getType())
                .donorIspb(Integer.parseInt(claimerParticipant))
                .branchNumber(claimerBranch)
                .accountNumber(claimerAccountNumber)
                .accountType(claimerAccountType.getType())
                .accountOpeningDate(claimerAccountOpeningDate)
                .personType(claimerType.getPersonType())
                .cpfCnpj(claimerTaxId)
                .name(claimerName)
                .donorIspb(Integer.parseInt(donorParticipant))
                .claimSituation(status.getClaimSituation())
                .completionThresholdDate(completionPeriodEnd)
                .resolutionThresholdDate(resolutionPeriodEnd)
                .lastModifiedDate(lastModified)
                .build();
    }

}
