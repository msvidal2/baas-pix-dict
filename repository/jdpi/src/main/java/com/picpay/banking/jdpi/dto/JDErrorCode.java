package com.picpay.banking.jdpi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum JDErrorCode {

    // Módulo: DICT
    Forbidden("Forbidden", "Requisição de participante autenticado que viola alguma regra de autorização."),
    BadRequest("BadRequest", "Requisição com formato inválido."),
    NotFound("NotFound", "Entidade não encontrada."),
    RateLimited("RateLimited", "Limite de requisições foi atingido. Ver seção sobre limitação de requisições."),
    InternalServerError("InternalServerError", "Condição inesperada ao processar requisição."),
    ServiceUnavailable("ServiceUnavailable", "Serviço não está disponível no momento. Serviço solicitado pode estar em manutenção ou fora da janela de funcionamento."),
    RequestSignatureInvalid("RequestSignatureInvalid", "Assinatura digital da requisição enviada é inválida."),
    RequestOnBehalfUnauthorized("RequestOnBehalfUnauthorized", "Participante direto envia requisição em nome de participante indireto para o qual não tem autorização."),
    RequestIdAlreadyUsed("RequestIdAlreadyUsed", "Requisição foi feita com mesmo RequestId de requisição feita anteriormente, mas com parâmetros diferentes."),
    InvalidReason("InvalidReason", "Requisição foi feita com uma razão inválida para a operação."),
    EntryInvalid("EntryInvalid", "Existem campos inválidos ao tentar criar novo vínculo."),
    EntryLimitExceeded("EntryLimitExceeded", "Número de vínculos associados a conta transacional excedeu o limite máximo."),
    EntryAlreadyExists("EntryAlreadyExists", "Já existe vínculo para essa chave com o mesmo participante e dono."),
    EntryCannotBeQueriedForBookTransfer("EntryCannotBeQueriedForBookTransfer", "Vínculo consultado está custodiado no mesmo PSP do usuário pagador para quem está sendo feita a consulta. Quando o pagador e o recebedor estão no mesmo PSP, não deve ser feita consulta ao DICT."),
    EntryKeyOwnedByDifferentPerson("EntryKeyOwnedByDifferentPerson", "Já existe vínculo para essa chave mas ela é possuída por outra pessoa. Indica-se que seja feita uma reivindicação de posse."),
    EntryKeyInCustodyOfDifferentParticipant("EntryKeyInCustodyOfDifferentParticipant", "á existe vínculo para essa chave com o mesmo dono, mas ela encontra-se associada a outro participante. Indica-se que seja feita uma reivindicação de portabilidade."),
    EntryLockedByClaim("EntryLockedByClaim", "Existe uma reivindicação com status diferente de concluída ou cancelada para a chave do vínculo. Enquanto estiver nessa situação, o vínculo não pode ser excluído."),
    ClaimInvalid("ClaimInvalid", "Existem campos inválidos ao tentar criar nova reivindicação."),
    ClaimTypeInconsistent("ClaimTypeInconsistent", "Tipo de reivindicação pedida é inconsistente. Esse erro ocorre nas situações em que se tenta criar a) reivindicação de posse, mas vínculo existente tem como dona a mesma pessoa que reivindica ou b) reinvidicação de portabilidade, mas vínculo existente tem como dona pessoa diferente da que reivindica."),
    ClaimKeyNotFound("ClaimKeyNotFound", "Não existe vínculo registrado com a chave que está sendo reivindicada."),
    ClaimAlreadyExistsForKey("ClaimAlreadyExistsForKey", "Existe uma reivindicação com status diferente de concluída ou cancelada para a chave reivindicada. Nova reivindicação para a chave só pode ser criada se a atual foi concluída ou cancelada."),
    ClaimResultingEntryAlreadyExists("ClaimResultingEntryAlreadyExists", "Vínculo que resultaria ao processar reivindicação já existe, com mesma chave, participante e dono."),
    ClaimOperationInvalid("ClaimOperationInvalid", "Status atual da reivindicação não permite que operação seja feita."),
    ClaimResolutionPeriodNotEnded("ClaimResolutionPeriodNotEnded", "Para reivindicação de posse, PSP doador não pode confirmar antes do término do período resolução. Para portabilidade, PSP doador não pode cancelar por fim de prazo antes do término do período resolução."),
    ClaimCompletionPeriodNotEnded("ClaimCompletionPeriodNotEnded", "Para reivindicação de posse, se PSP reivindicador tenta encerrar antes do término do período encerramento."),
    InfractionReportInvalid("InfractionReportInvalid", "Existem campos inválidos ao tentar criar o relato de infração."),
    InfractionReportOperationInvalid("InfractionReportOperationInvalid", "Status atual do relato não permite que operação seja feita."),
    InfractionReportTransactionNotFound("InfractionReportTransactionNotFound", "A transação listada no relato de infração não foi encontrada."),
    InfractionReportAlreadyBeingProcessedForTransaction("InfractionReportAlreadyBeingProcessedForTransaction", "Já existe um relato de infração em andamento para a transação informada."),
    InfractionReportAlreadyProcessedForTransaction("InfractionReportAlreadyProcessedForTransaction", "Já existe um relato de infração fechado para a transação informada."),
    InfractionReportPeriodExpired("InfractionReportPeriodExpired", "O prazo para o relato de infração sobre a transação expirou."),

    // Módulo: Chave de Endereçamento
    JDPICHV001("CHV001", "Chave de Endereçamento não encontrada."),
    JDPICHV002("CHV002", "Nenhuma chave de endereçamento vinculada a conta informada."),
    JDPICHV003("CHV003", "Nenhuma chave de endereçamento vinculada ao CPF/CNPJ informado."),
    JDPICHV004("CHV004", "Chave informada já existe na base de dados."),
    JDPICHV005("CHV005", "Já existe vínculo para essa chave neste PSP, mas ela é possuída por outra pessoa. Indica-se que seja feita uma reivindicação de posse."),
    JDPICHV006("CHV006", "Já existe vínculo para essa chave neste PSP com o mesmo dono e outra informação de conta. Indica-se que seja feita uma alteração de chave."),

    // Reivindicações
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
    JDPIRVN013("RVN013", "A chave já pertence ao cliente. Se necessário utilizar a alteração de chave para alterar a conta vinculada à chave."),
    JDPIRVN014("RVN014", "Já existe uma Reivindicação aberta para a chave."),

    // Relatos de Infração
    JDPIRIN001("RIN001", "A transação informada no Relato de Infração não foi encontrada."),
    JDPIRIN002("RIN002", "O prazo para o Relato de Infração sobre a transação expirou."),
    JDPIRIN003("RIN003", "Já existe um relato de infração fechado para a transação informada."),
    JDPIRIN004("RIN004", "Já existe um relato de infração em andamento para a transação informada."),
    JDPIRIN005("RIN005", "A situação do Relato de Infração não permite o seu cancelamento."),
    JDPIRIN006("RIN006", "O Relato de Infração só pode ser cancelado pelo PSP relator."),
    JDPIRIN007("RIN007", "O Relato de Infração não pode ser analisado pelo PSP relator.");

    private String code;

    private String message;

    public static JDErrorCode resolve(final String code) {
        return Stream.of(values())
                .filter(v -> v.name().equalsIgnoreCase(code))
                .findAny()
                .orElse(null);
    }

}
