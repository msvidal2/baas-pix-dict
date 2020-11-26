package com.picpay.banking.pixkey.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.Statistic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 19/11/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticsResponse {

    @XmlElement(name = "LastUpdated")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastUpdated;

    @XmlElementWrapper(name = "Counters")
    @XmlElement(name = "Counter")
    private List<Counter> counters;

    public Statistic toDomain() {
        return Statistic.builder()
                .lastUpdateDateAntiFraud(lastUpdated)
                .accountants(counters
                        .stream().map(counter -> counter.toDomain())
                        .collect(Collectors.toList())
                )
                .build();
    }

}
