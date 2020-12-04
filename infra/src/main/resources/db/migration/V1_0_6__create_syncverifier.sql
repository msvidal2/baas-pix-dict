alter table dict.pix_key
    add column cid character varying(64) not null;
alter table dict.pix_key
    add constraint pix_key_uk_cid unique (cid);
comment on column dict.pix_key.cid is 'O CID é o content identifier. É a representação hexadecimal, em lowercase, do resultado da função hmacSha256 de determinados campos da chave para manter o sincronismo.';

create table dict.sync_verifier
(
    key_type        character varying(20)       not null primary key,
    vsync           character varying(64)       not null,
    synchronized_at timestamp without time zone not null
);
comment on table dict.sync_verifier is 'Responsável por armazenar apenas os últimos sincronismos que tiveram sucesso. Essa tabela nunca tera mais de 5 linhas, uma para cada tipo de chave.';
comment on column dict.sync_verifier.key_type is 'O keyType é o tipo da chave utilizada no Pix. Novos tipos poderão surgir. Os valores atuais são: "CPF", "CNPJ", "PHONE", "EMAIL", "EVP"';
comment on column dict.sync_verifier.vsync is 'O VSync é resultado da aplicação de bitwise-XOR de todos os CIDs deste tipo de chave';
comment on column dict.sync_verifier.synchronized_at is 'Esta é a data que foi feita o sincronismo com o Banco Central';

create table dict.sync_verifier_historic
(
    id                 serial                      not null primary key,
    key_type           character varying(20)       not null,
    vsync_start        character varying(64)       not null,
    vsync_end          character varying(64)       not null,
    synchronized_start timestamp without time zone not null,
    synchronized_end   timestamp without time zone not null,
    result             character varying(20)       not null
);
comment on table dict.sync_verifier_historic is 'Responsável por armazenar todos os eventos de sincronização, fornecendo informações detalhadas sobre o resultado e ações impostas do sincronismo';
comment on column dict.sync_verifier_historic.id is 'Chave primária sequencial';
comment on column dict.sync_verifier_historic.vsync_start is 'Valor do vsync quando o processo de sincronização começou';
comment on column dict.sync_verifier_historic.vsync_end is 'Valor do vsync quando o processo de sincronização terminou. Este é o valor que foi transmitido para o Banco Central no sincronismo';
comment on column dict.sync_verifier_historic.synchronized_start is 'Data do ultimo sincronismo que teve sucesso. Utilizada para filtrar todos CIDs apos ela';
comment on column dict.sync_verifier_historic.synchronized_end is 'Data que terminou o processo de sincronismo';
comment on column dict.sync_verifier_historic.result is 'Enum com o tipo de resultado: OK, NOK';


create table dict.content_identifier_event
(
    id                 serial                      not null primary key,
    pix_key            character varying(100)      not null,
    key_type           character varying(20)       not null,
    cid                character varying(64)       not null,
    created_at         timestamp without time zone not null,
    key_ownership_date timestamp without time zone not null,
    type               character varying(20)       not null
);
comment on table dict.content_identifier_event is 'Armazena todos os eventos de manipulação do cadastro de chaves, gerando um cid para ser utilizado na sincronização de base';
comment on column dict.content_identifier_event.id is 'Chave primária sequencial';
comment on column dict.content_identifier_event.pix_key is 'Key da chave em questão. Não existe uma chave estrangeira porque esta tabela de eventos deve manter os dados mesmo quando ocorre um delete na outra tabela de cadastro de chaves';
comment on column dict.content_identifier_event.key_type is 'O keyType é o tipo da chave utilizada no Pix. Novos tipos poderão surgir. Os valores atuais são: "CPF", "CNPJ", "PHONE", "EMAIL", "EVP"';
comment on column dict.content_identifier_event.cid is 'O CID é o content identifier. É a representação hexadecimal, em lowercase, do resultado da função hmacSha256 de determinados campos da chave para manter o sincronismo.';
comment on column dict.content_identifier_event.created_at is 'Data em que o evento foi persistido. Não é a data exata de alteração no Banco Central';
comment on column dict.content_identifier_event.key_ownership_date is 'Data em que o dono da chave tem posse do vinculo. Essa data é controlada no Banco Central e é a data utilizada para o sincronismo por eventos.';
comment on column dict.content_identifier_event.type is 'Enum com o tipo de operação realizada: ADDED, REMOVED';

create table dict.sync_verifier_historic_action
(
    id                        serial                not null primary key,
    id_sync_verifier_historic integer               not null,
    cid                       character varying(64) not null,
    action                    character varying(20) not null,
    constraint fk_sync_verifier foreign key (id_sync_verifier_historic) references dict.sync_verifier_historic (id)
);
comment on table dict.sync_verifier_historic_action is 'Armazena todas ações que devem ser feitas para correção dos dados para sincronismo da base';
comment on column dict.sync_verifier_historic_action.id is 'Chave primária sequencial';
comment on column dict.sync_verifier_historic_action.cid is 'O CID é o content identifier. É a representação hexadecimal, em lowercase, do resultado da função hmacSha256 de determinados campos da chave para manter o sincronismo.';
comment on column dict.sync_verifier_historic_action.id_sync_verifier_historic is 'Indica qual o histórico de sincronismo que deu origem a esta ação';
comment on column dict.sync_verifier_historic_action.action is 'Enum com o tipo de ação corretiva: ADDED, REMOVED';
