package com.picpay.banking.fallbacks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum BacenErrorCode {

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
    InfractionReportTransactionNotSettled("InfractionReportTransactionNotSettled", "Já existe uma infração aberta e não fechada para essa transação.");

    private String code;

    private String message;

    public static BacenErrorCode resolve(final String code) {
        return Stream.of(values())
                .filter(v -> v.name().equalsIgnoreCase(code))
                .findAny()
                .orElse(null);
    }

}
