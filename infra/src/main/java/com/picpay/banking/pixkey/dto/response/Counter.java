package com.picpay.banking.pixkey.dto.response;

import com.picpay.banking.pix.core.domain.Accountant;
import com.picpay.banking.pix.core.domain.AccountantType;
import com.picpay.banking.pix.core.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

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
@XmlRootElement(name = "Counter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Counter {

    @XmlAttribute(name = "type")
    private CounterType type;

    @XmlAttribute(name = "by")
    private CounterByType by;

    @XmlAttribute(name = "d3")
    private Integer d3;

    @XmlAttribute(name = "d30")
    private Integer d30;

    @XmlAttribute(name = "m6")
    private Integer m6;

    public Accountant toDomain() {
        return Accountant
                .builder()
                .type(AccountantType.resolve(type.getValue()))
                .aggregate(Aggregate.resolve(by.getValue()))
                .lastThreeDays(d3)
                .lastThirtyDays(d30)
                .lastSixMonths(m6)
                .build();
    }

}
