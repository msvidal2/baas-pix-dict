package com.picpay.banking.fallbacks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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
    private List<Violation> violations;

}
