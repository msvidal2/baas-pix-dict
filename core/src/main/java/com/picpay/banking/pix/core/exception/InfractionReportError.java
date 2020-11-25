/*
 *  baas-pix-dict 1.0 11/24/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rafael.braga
 * @version 1.0 24/11/2020
 */
@Getter
@AllArgsConstructor
public enum InfractionReportError {

    JDPIRIN001("RIN001", "A transação informada no Relato de Infração não foi encontrada."),
    JDPIRIN002("RIN002", "O prazo para o Relato de Infração sobre a transação expirou."),
    JDPIRIN003("RIN003", "Já existe um relato de infração fechado para a transação informada."),
    JDPIRIN004("RIN004", "Já existe um relato de infração em andamento para a transação informada."),
    JDPIRIN005("RIN005", "A situação do Relato de Infração não permite o seu cancelamento."),
    JDPIRIN006("RIN006", "O Relato de Infração só pode ser cancelado pelo PSP relator."),
    JDPIRIN007("RIN007", "O Relato de Infração não pode ser analisado pelo PSP relator."),
    INFRACTION_REPORT_CONFLICT("RIN008", "Já foi criado um relato de infração com esse request identifier com body diferente"),
    INFRACTION_REPORT_NOT_FOUND("RIN009", "Não foi localizada a infração informada");

    private final String code;
    private final String message;

}
