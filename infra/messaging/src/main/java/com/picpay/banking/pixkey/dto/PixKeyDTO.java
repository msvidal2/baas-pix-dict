package com.picpay.banking.pixkey.dto;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Statistic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PixKeyDTO {

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
    private Statistic statistic;
    private String cid;
    private UUID requestId;
    private boolean donatedAutomatically;

    public static Object from(final PixKey pixKey) {
        return PixKeyDTO.builder()
            .type(pixKey.getType())
            .key(pixKey.getKey())
            .ispb(pixKey.getIspb())
            .nameIspb(pixKey.getNameIspb())
            .branchNumber(pixKey.getBranchNumber())
            .accountType(pixKey.getAccountType())
            .accountNumber(pixKey.getAccountNumber())
            .accountOpeningDate(pixKey.getAccountOpeningDate())
            .personType(pixKey.getPersonType())
            .taxId(pixKey.getTaxId())
            .name(pixKey.getName())
            .fantasyName(pixKey.getFantasyName())
            .createdAt(pixKey.getCreatedAt())
            .updatedAt(pixKey.getUpdatedAt())
            .startPossessionAt(pixKey.getStartPossessionAt())
            .endToEndId(pixKey.getEndToEndId())
            .correlationId(pixKey.getCorrelationId())
            .claim(pixKey.getClaim())
            .statistic(pixKey.getStatistic())
            .cid(pixKey.getCid())
            .requestId(pixKey.getRequestId())
            .donatedAutomatically(pixKey.isDonatedAutomatically())
            .build();
    }

}
