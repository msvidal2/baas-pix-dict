package com.picpay.banking.claim.dto.response;

import com.picpay.banking.claim.dto.request.ClaimType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pixkey.dto.request.Account;
import com.picpay.banking.pixkey.dto.request.KeyType;
import com.picpay.banking.pixkey.dto.request.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ClaimResponse {

    @XmlElement(name = "Type")
    private ClaimType type;

    @XmlElement(name = "Key")
    private String key;

    @XmlElement(name = "KeyType")
    private KeyType keyType;

    @XmlElement(name = "ClaimerAccount")
    private Account claimerAccount;

    @XmlElement(name = "Claimer")
    private Owner claimer;

    @XmlElement(name = "DonorParticipant")
    private String donorParticipant;

    @XmlElement(name = "Id")
    private String id;

    @XmlElement(name = "Status")
    private ClaimStatus status;

    @XmlElement(name = "CompletionPeriodEnd")
    private LocalDateTime completionPeriodEnd;

    @XmlElement(name = "ResolutionPeriodEnd")
    private LocalDateTime resolutionPeriodEnd;

    @XmlElement(name = "LastModified")
    private LocalDateTime lastModified;

    public static ClaimResponse from(Claim claim) {
        return ClaimResponse.builder()
                .type(ClaimType.resolve(claim.getClaimType()))
                .key(claim.getKey())
                .keyType(KeyType.resolve(claim.getKeyType()))
                .claimerAccount(Account.from(claim))
                .claimer(Owner.from(claim))
                .build();
    }

    public Claim toClaim() {
        return Claim.builder()
                .claimId(id)
                .claimType(type.getClaimType())
                .key(key)
                .keyType(keyType.getType())
                .ispb(Integer.parseInt(claimerAccount.getParticipant()))
                .branchNumber(claimerAccount.getBranch())
                .accountNumber(claimerAccount.getAccountNumber())
                .accountType(claimerAccount.getAccountType().getType())
                .accountOpeningDate(claimerAccount.getOpeningDate())
                .cpfCnpj(claimer.getTaxIdNumber())
                .name(claimer.getName())
                .donorIspb(Integer.parseInt(donorParticipant))
                .claimSituation(status.getClaimSituation())
                .completionThresholdDate(completionPeriodEnd)
                .resolutionThresholdDate(resolutionPeriodEnd)
                .lastModifiedDate(lastModified)
                .personType(claimer.getType().getPersonType())
                .build();
    }

}
