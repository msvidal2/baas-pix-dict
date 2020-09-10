package com.picpay.banking.jdpi.dto.response;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer cpfCnpj;
    private String name;
    private String fantasyName;
    private LocalDateTime createdAt;
    private LocalDateTime startPossessionAt;
    private String endToEndId;
    private StatisticResponseDTO statistic;

}
