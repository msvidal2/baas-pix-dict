package com.picpay.banking.pixkey.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

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

}
