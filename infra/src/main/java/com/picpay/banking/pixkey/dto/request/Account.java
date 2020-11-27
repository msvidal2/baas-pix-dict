package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.PixKey;
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
public class Account {

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "Branch")
    private String branch;

    @XmlElement(name = "AccountNumber")
    private String accountNumber;

    @XmlElement(name = "AccountType")
    private AccountType accountType;

    @XmlElement(name = "OpeningDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime openingDate;

    public static Account from(PixKey pixKey) {
        return Account.builder()
                .participant(String.valueOf(pixKey.getIspb()))
                .branch(pixKey.getBranchNumber())
                .accountNumber(pixKey.getAccountNumber())
                .accountType(AccountType.resolve(pixKey.getAccountType()))
                .openingDate(pixKey.getAccountOpeningDate())
                .build();
    }

    public static Account from(Claim claim) {
        return Account.builder()
                .participant(String.valueOf(claim.getIspb()))
                .branch(claim.getBranchNumber())
                .accountNumber(claim.getAccountNumber())
                .accountType(AccountType.resolve(claim.getAccountType()))
                .openingDate(claim.getAccountOpeningDate())
                .build();
    }

}
