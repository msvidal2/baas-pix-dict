package com.picpay.banking.pix.infra.openapi.msg;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Builder
@Setter
@Getter
public class AddressingKeyControllerMessages {

    public static final String CLASS_CONTROLLER = "AddressingKey";

    public static final String METHOD_CREATE = "Criar uma nova Chave de Endereçamento.";
    public static final String METHOD_LIST = "Listar Chaves de Endereçamento por cliente.";
    public static final String METHOD_FIND = "Obter Chave de Endereçamento por chave.";
    public static final String METHOD_DELETE = "Deletar uma Chave de Endereçamento por chave.";
    public static final String METHOD_UPDATE_ACCOUNT = "Atualizar Chave de Endereçamento.";

}
