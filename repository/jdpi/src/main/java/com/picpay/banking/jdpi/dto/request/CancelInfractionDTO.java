package com.picpay.banking.jdpi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CancelInfractionDTO {

    private String idRelatoInfracao;

    private Integer ispb;

}
