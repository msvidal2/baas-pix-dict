package com.picpay.banking.pix.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PixKeyError {

    KEY_NOT_FOUND("CHV001", "Chave de Endereçamento não encontrada."),
    NO_KEYS_TO_ACCOUNT("CHV002", "Nenhuma chave de endereçamento vinculada a conta informada."),
    NO_KEYS_TO_DOCUMENT("CHV003", "Nenhuma chave de endereçamento vinculada ao CPF/CNPJ informado."),
    KEY_EXISTS("CHV004", "Chave informada já existe na base de dados."),
    KEY_EXISTS_INTO_PSP_TO_ANOTHER_PERSON("CHV005", "Já existe vínculo para essa chave neste PSP, mas ela é possuída por outra pessoa. Indica-se que seja feita uma reivindicação de posse."),
    KEY_EXISTS_INTO_PSP_TO_SAME_PERSON("CHV006", "Já existe vínculo para essa chave neste PSP com o mesmo dono e outra informação de conta. Indica-se que seja feita uma alteração de chave."),
    EXISTING_ACCOUNT_REGISTRATION_FOR_ANOTHER_PERSON("CHV007", "Já existe um registro da conta informada para outra pessoa."),
    CLAIM_PROCESS_EXISTING("CHV008", "A chave não pode ser cadastrada, devido a existência de um processo de reivindicação para a mesma.");

    private final String code;
    private final String message;

}
