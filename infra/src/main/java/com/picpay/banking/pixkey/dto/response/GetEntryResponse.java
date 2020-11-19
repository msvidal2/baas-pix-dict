package com.picpay.banking.pixkey.dto.response;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pixkey.dto.request.Entry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/11/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GetEntryResponse {

    @XmlElement(name = "ResponseTime")
    private LocalDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    @XmlElement(name = "Entry")
    private Entry entry;

    @XmlElement(name = "Statistics")
    private StatisticsResponse statistics;

    public PixKey toPixKey() {
        // TODO como incluir os atributos que estão comentados???
        return PixKey.builder()
                .type(KeyType.resolve(entry.getKeyType().getValue()))
                .key(entry.getKey())
                .ispb(Integer.parseInt(entry.getAccount().getParticipant()))
//                .nameIspb()
                .branchNumber(entry.getAccount().getBranch())
                .accountType(AccountType.resolve(entry.getAccount().getAccountType().getValue()))
                .accountNumber(entry.getAccount().getAccountNumber())
                .accountOpeningDate(entry.getAccount().getOpeningDate())
                .personType(PersonType.resolve(entry.getOwner().getType().getValue()))
                .taxId(entry.getOwner().getTaxIdNumber())
                .name(entry.getOwner().getName())
//                .fantasyName()
                .createdAt(entry.getCreationDate())
                .startPossessionAt(entry.getKeyOwnershipDate())
//                .endToEndId()
                .correlationId(correlationId)
//                .claim()
                .statistic(
                        Statistic.builder()
                                .lastUpdateDateAntiFraud(statistics.getLastUpdated())
                                .accountants(statistics.getCounters()
                                        .stream().map(counter -> Accountant
                                                .builder()
//                                                .type()
                                                .aggregate(Aggregate.resolve(counter.getBy().getValue()))
                                                .lastThreeDays(counter.getD3())
                                                .lastThirtyDays(counter.getD30())
                                                .lastSixMonths(counter.getM6())
                                                .build()
                                        ).collect(Collectors.toList())
                                )
                                .build()
                )
                .build();
    }
}
