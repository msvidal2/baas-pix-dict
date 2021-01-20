package com.picpay.banking.fallbacks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum BacenErrorCode {

    FORBIDDEN("Forbidden", "Requisição de participante autenticado que viola alguma regra de autorização."),
    BAD_REQUEST("BadRequest", "Requisição com formato inválido."),
    NOT_FOUND("NotFound", "Entidade não encontrada."),
    RATE_LIMITED("RateLimited", "Limite de requisições foi atingido. Ver seção sobre limitação de requisições."),
    INTERNAL_SERVER_ERROR("InternalServerError", "Condição inesperada ao processar requisição."),
    SERVICE_UNAVAILABLE("ServiceUnavailable", "Serviço não está disponível no momento. Serviço solicitado pode estar em manutenção ou fora da janela de funcionamento."),
    REQUEST_SIGNATURE_INVALID("RequestSignatureInvalid", "Assinatura digital da requisição enviada é inválida."),
    REQUEST_ON_BEHALF_UNAUTHORIZED("RequestOnBehalfUnauthorized", "Participante direto envia requisição em nome de participante indireto para o qual não tem autorização."),
    REQUEST_ID_ALREADY_USED("RequestIdAlreadyUsed", "Requisição foi feita com mesmo RequestId de requisição feita anteriormente, mas com parâmetros diferentes."),
    INVALID_REASON("InvalidReason", "Requisição foi feita com uma razão inválida para a operação."),
    ENTRY_INVALID("EntryInvalid", "Existem campos inválidos ao tentar criar novo vínculo."),
    ENTRY_LIMIT_EXCEEDED("EntryLimitExceeded", "Número de vínculos associados a conta transacional excedeu o limite máximo."),
    ENTRY_ALREADY_EXISTS("EntryAlreadyExists", "Já existe vínculo para essa chave com o mesmo participante e dono."),
    ENTRY_CANNOT_BE_QUERIED_FOR_BOOK_TRANSFER("EntryCannotBeQueriedForBookTransfer", "Vínculo consultado está custodiado no mesmo PSP do usuário pagador para quem está sendo feita a consulta. Quando o pagador e o recebedor estão no mesmo PSP, não deve ser feita consulta ao DICT."),
    ENTRY_KEY_OWNED_BY_DIFFERENT_PERSON("EntryKeyOwnedByDifferentPerson", "Já existe vínculo para essa chave mas ela é possuída por outra pessoa. Indica-se que seja feita uma reivindicação de posse."),
    ENTRY_KEY_IN_CUSTODY_OF_DIFFERENT_PARTICIPANT("EntryKeyInCustodyOfDifferentParticipant", "á existe vínculo para essa chave com o mesmo dono, mas ela encontra-se associada a outro participante. Indica-se que seja feita uma reivindicação de portabilidade."),
    ENTRY_LOCKED_BY_CLAIM("EntryLockedByClaim", "Existe uma reivindicação com status diferente de concluída ou cancelada para a chave do vínculo. Enquanto estiver nessa situação, o vínculo não pode ser excluído."),
    CLAIM_INVALID("ClaimInvalid", "Existem campos inválidos ao tentar criar nova reivindicação."),
    CLAIM_TYPE_INCONSISTENT("ClaimTypeInconsistent", "Tipo de reivindicação pedida é inconsistente. Esse erro ocorre nas situações em que se tenta criar a) reivindicação de posse, mas vínculo existente tem como dona a mesma pessoa que reivindica ou b) reinvidicação de portabilidade, mas vínculo existente tem como dona pessoa diferente da que reivindica."),
    CLAIM_KEY_NOT_FOUND("ClaimKeyNotFound", "Não existe vínculo registrado com a chave que está sendo reivindicada."),
    CLAIM_ALREADY_EXISTS_FOR_KEY("ClaimAlreadyExistsForKey", "Existe uma reivindicação com status diferente de concluída ou cancelada para a chave reivindicada. Nova reivindicação para a chave só pode ser criada se a atual foi concluída ou cancelada."),
    CLAIM_RESULTING_ENTRY_ALREADY_EXISTS("ClaimResultingEntryAlreadyExists", "Vínculo que resultaria ao processar reivindicação já existe, com mesma chave, participante e dono."),
    CLAIM_OPERATION_INVALID("ClaimOperationInvalid", "Status atual da reivindicação não permite que operação seja feita."),
    CLAIM_RESOLUTION_PERIOD_NOT_ENDED("ClaimResolutionPeriodNotEnded", "Para reivindicação de posse, PSP doador não pode confirmar antes do término do período resolução. Para portabilidade, PSP doador não pode cancelar por fim de prazo antes do término do período resolução."),
    CLAIM_COMPLETION_PERIOD_NOT_ENDED("ClaimCompletionPeriodNotEnded", "Para reivindicação de posse, se PSP reivindicador tenta encerrar antes do término do período encerramento."),
    INFRACTION_REPORT_INVALID("InfractionReportInvalid", "Existem campos inválidos ao tentar criar o relato de infração."),
    INFRACTION_REPORT_OPERATION_INVALID("InfractionReportOperationInvalid", "Status atual do relato não permite que operação seja feita."),
    INFRACTION_REPORT_TRANSACTION_NOT_FOUND("InfractionReportTransactionNotFound", "A transação listada no relato de infração não foi encontrada."),
    INFRACTION_REPORT_ALREADY_BEING_PROCESSED_FOR_TRANSACTION("InfractionReportAlreadyBeingProcessedForTransaction", "Já existe um relato de infração em andamento para a transação informada."),
    INFRACTION_REPORT_ALREADY_PROCESSED_FOR_TRANSACTION("InfractionReportAlreadyProcessedForTransaction", "Já existe um relato de infração fechado para a transação informada."),
    INFRACTION_REPORT_PERIOD_EXPIRED("InfractionReportPeriodExpired", "O prazo para o relato de infração sobre a transação expirou."),
    INFRACTION_REPORT_TRANSACTION_NOT_SETTLED("InfractionReportTransactionNotSettled", "A transação definida no relato de infração não foi liquidada.");

    private String code;

    private String message;

    public static BacenErrorCode resolve(final String code) {
        return Stream.of(values())
                .filter(v -> v.name().equalsIgnoreCase(code))
                .findAny()
                .orElse(null);
    }

}
