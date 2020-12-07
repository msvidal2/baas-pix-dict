package com.picpay.banking.claim.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.claim.dto.ClaimReason;
import com.picpay.banking.claim.dto.request.ClaimType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pixkey.dto.request.Account;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.pixkey.dto.request.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
    private KeyTypeBacen keyType;

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
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime completionPeriodEnd;

    @XmlElement(name = "ResolutionPeriodEnd")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime resolutionPeriodEnd;

    @XmlElement(name = "LastModified")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastModified;

    @XmlElement(name = "ConfirmReason")
    private ClaimReason confirmReason;

    @XmlElement(name = "CancelReason")
    private ClaimReason cancelReason;

    @XmlElement(name = "CancelledBy")
    private CancelledBy cancelledBy;

    public static ClaimResponse from(Claim claim) {
        return ClaimResponse.builder()
                .type(ClaimType.resolve(claim.getClaimType()))
                .key(claim.getKey())
                .keyType(KeyTypeBacen.resolve(claim.getKeyType()))
                .claimerAccount(Account.from(claim))
                .claimer(Owner.from(claim))
                .build();
    }

    public Claim toClaim() {
        return toClaim(null);
    }

    public Claim toClaim(final String correlationId) {
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
                .cancelReason(cancelReason != null ? cancelReason.getCancelReason() : null)
                .confirmationReason(confirmReason != null ? confirmReason.getConfirmationReason() : null)
                .isClaim(cancelledBy != null ? cancelledBy.isValue() : null)
                .correlationId(correlationId)
                .build();
    }

}
