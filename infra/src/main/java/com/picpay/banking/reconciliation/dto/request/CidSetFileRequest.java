package com.picpay.banking.reconciliation.dto.request;

import com.picpay.banking.pixkey.dto.request.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CreateCidSetFileRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CidSetFileRequest {

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "KeyType")
    private KeyType keyType;

}
