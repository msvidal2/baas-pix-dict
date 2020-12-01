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

    //Criação
    REPORTED_TRANSACTION_NOT_FOUND("RIN001", "A transação informada no Relato de Infração não foi encontrada."),
    EXPIRED_REPORT_PERIOD("RIN002", "O prazo para o Relato de Infração sobre a transação expirou."),
    INFRACTION_REPORT_CLOSED("RIN003", "Já existe um relato de infração fechado para a transação informada."),
    INFRACTION_REPORT_ALREADY_OPEN("RIN004", "Já existe um relato de infração em andamento para a transação informada."),
    //Cancelamento
    CANNOT_CANCEL_INFRACTION_REPORT("RIN005", "A situação do Relato de Infração não permite o seu cancelamento."),
    INFRACTION_REPORT_SHOULD_BE_CANCELLED_BY_SUBMITTER("RIN006", "O Relato de Infração só pode ser cancelado pelo PSP relator."),
    //Análise,
    INFRACTION_REPORT_CANT_BE_ANALYZED_BY_SUBMITTER("RIN007", "O Relato de Infração não pode ser analisado pelo PSP relator."),
    //Localizar (Find)
    INFRACTION_REPORT_NOT_FOUND("RIN009", "Não foi localizada a infração informada");

    private final String code;
    private final String message;

}
