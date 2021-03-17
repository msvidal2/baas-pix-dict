package com.picpay.banking.pix.core.events.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PixKeyEventData implements Serializable {

    private KeyType type;
    private String key;
    private Integer ispb;
    private String nameIspb;
    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private PersonType personType;
    private String taxId;
    private String name;
    private String fantasyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startPossessionAt;
    private String endToEndId;
    private String correlationId;
    private ClaimType claim;
    private String cid;
    private UUID requestId;
    private Boolean donatedAutomatically;
    private PixKeySituation situation;
    private Reason reason;

    public PixKey toPixKey() {
        return PixKey.builder()
                .type(type)
                .key(key)
                .ispb(ispb)
                .branchNumber(branchNumber)
                .accountType(accountType)
                .accountNumber(accountNumber)
                .accountOpeningDate(accountOpeningDate)
                .personType(personType)
                .taxId(taxId)
                .name(name)
                .fantasyName(fantasyName)
                .situation(PixKeySituation.OPEN)
                .build();
    }

    public static PixKeyEventData from(final PixKey pixKey, final Reason reason) {
        return PixKeyEventData.builder()
                .type(pixKey.getType())
                .key(pixKey.getKey())
                .ispb(pixKey.getIspb())
                .branchNumber(pixKey.getBranchNumber())
                .accountType(pixKey.getAccountType())
                .accountNumber(pixKey.getAccountNumber())
                .accountOpeningDate(pixKey.getAccountOpeningDate())
                .personType(pixKey.getPersonType())
                .taxId(pixKey.getTaxId())
                .name(pixKey.getName())
                .createdAt(pixKey.getCreatedAt())
                .startPossessionAt(pixKey.getStartPossessionAt())
                .correlationId(pixKey.getCorrelationId())
                .requestId(pixKey.getRequestId())
                .cid(pixKey.getCid())
                .updatedAt(pixKey.getUpdatedAt())
                .donatedAutomatically(pixKey.getDonatedAutomatically())
                .fantasyName(pixKey.getFantasyName())
                .build();
    }

}
