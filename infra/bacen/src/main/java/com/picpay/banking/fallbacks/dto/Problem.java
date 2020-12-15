package com.picpay.banking.fallbacks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "problem")
@XmlAccessorType(XmlAccessType.FIELD)
public class Problem {

    private String type;

    private String title;

    private Integer status;

    private String detail;

    private String correlationId;

    @XmlElementWrapper(name = "violations")
    @XmlElement(name = "violation")
    private List<Violation> violations;

}
