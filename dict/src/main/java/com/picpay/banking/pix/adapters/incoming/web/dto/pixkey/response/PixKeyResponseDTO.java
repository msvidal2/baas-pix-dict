package com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class PixKeyResponseDTO {

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
    private LocalDateTime startPossessionAt;
    private String endToEndId;
    private ClaimType claim;
    private Statistic statistic;
    private PixKeySituation situation;

    public static PixKeyResponseDTO from(final PixKey pixKey) {
        return PixKeyResponseDTO.builder()
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
                .startPossessionAt(pixKey.getStartPossessionAt())
                .endToEndId(pixKey.getEndToEndId())
                .claim(pixKey.getClaim())
                .statistic(pixKey.getStatistic())
                .situation(pixKey.getSituation())
                .build();
    }

}
