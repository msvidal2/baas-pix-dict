package com.picpay.banking.pix.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimError {

    JDPIRVN001("RVN001", "O Identificador da Reivindicação não foi encontrado."),
    JDPIRVN002("RVN002", "A situação da sua Reivindicação não permite o seu cancelamento."),
    JDPIRVN003("RVN003", "O motivo para cancelar a sua Reivindicação não é valido."),
    JDPIRVN004("RVN004", "A situação da Reivindicação de Posse de Chave que você recebeu, não permite o seu cancelamento."),
    JDPIRVN005("RVN005", "A situação da Portabilidade de Chave que você recebeu, não permite o seu cancelamento."),
    JDPIRVN006("RVN006", "O motivo para cancelar a Reivindicação que você recebeu não é válido."),
    JDPIRVN007("RVN007", "A Reivindicação não pode ser confirmada pelo reivindicador da chave."),
    JDPIRVN008("RVN008", "A situação da Reivindicação que você recebeu, não permite confirmação."),
    JDPIRVN009("RVN009", "O motivo para confirmar a Reivindicação que você recebeu não é válido."),
    JDPIRVN010("RVN010", "A Reivindicação já estava concluída."),
    JDPIRVN011("RVN011", "A situação da sua Reivindicação não permite a sua conclusão."),
    JDPIRVN012("RVN012", "A Reivindicação não pode ser concluída pelo doador da chave."),
    KEY_ALREADY_BELONGS_TO_CUSTOMER("RVN013", "Reivindicação de posse de chave que tem como dona a mesma pessoa que reivindica."),
    OPEN_CLAIM_ALREADY_EXISTS_FOR_KEY("RVN014", "Já existe uma Reivindicação aberta para a chave."),
    INCONSISTENT_PORTABILITY("RVN015", "Reivindicação de portabilidade que tem como dono uma pessoa diferente da que reivindica.");

    private String code;
    private String message;

}
