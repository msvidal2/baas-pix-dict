alter table pix_key
    add column cid character varying(64) not null
        comment 'O CID é o content identifier. É a representação hexadecimal, em lowercase, do resultado da função hmacSha256 de determinados campos da chave para manter o sincronismo.';
alter table pix_key
    add constraint pix_key_uk_cid unique (cid);

create table sync_verifier
(
    key_type        character varying(20) not null primary key comment 'O keyType é o tipo da chave utilizada no Pix. Novos tipos poderão surgir. Os valores atuais são: "CPF", "CNPJ", "PHONE", "EMAIL", "EVP"',
    vsync           character varying(64) not null comment 'O VSync é resultado da aplicação de bitwise-XOR de todos os CIDs deste tipo de chave',
    synchronized_at timestamp             not null comment 'Esta é a data que foi feita o sincronismo com o Banco Central',
    updated_at      timestamp             not null comment 'Esta é a data local que o sincronismo rodou pela ultima vez'
);
alter table sync_verifier
    comment 'Responsável por armazenar apenas os últimos sincronismos que tiveram sucesso. Essa tabela nunca tera mais de 5 linhas, uma para cada tipo de chave.';


create table sync_verifier_historic
(
    id                 integer auto_increment not null primary key comment 'Chave primária sequencial',
    key_type           character varying(20)  not null comment 'O keyType é o tipo da chave utilizada no Pix. Novos tipos poderão surgir. Os valores atuais são: "CPF", "CNPJ", "PHONE", "EMAIL", "EVP"',
    vsync_start        character varying(64)  not null comment 'Valor do vsync quando o processo de sincronização começou',
    vsync_end          character varying(64)  not null comment 'Valor do vsync quando o processo de sincronização terminou. Este é o valor que foi transmitido para o Banco Central no sincronismo',
    synchronized_start timestamp              not null comment 'Data do ultimo sincronismo que teve sucesso. Utilizada para filtrar todos CIDs apos ela',
    synchronized_end   timestamp              not null comment 'Data que terminou o processo de sincronismo',
    result             character varying(20)  not null comment 'Enum com o tipo de resultado: OK, NOK'
);
alter table sync_verifier_historic
    comment 'Responsável por armazenar todos os eventos de sincronização, fornecendo informações detalhadas sobre o resultado e ações impostas do sincronismo';


create table content_identifier_event
(
    id                integer auto_increment not null primary key comment 'Chave primária sequencial',
    pix_key           character varying(100) not null comment 'Key da chave em questão. Não existe uma chave estrangeira porque esta tabela de eventos deve manter os dados mesmo quando ocorre um delete na outra tabela de cadastro de chaves',
    key_type          character varying(20)  not null comment 'O keyType é o tipo da chave utilizada no Pix. Novos tipos poderão surgir. Os valores atuais são: "CPF", "CNPJ", "PHONE", "EMAIL", "EVP"',
    cid               character varying(64)  not null comment 'O CID é o content identifier. É a representação hexadecimal, em lowercase, do resultado da função hmacSha256 de determinados campos da chave para manter o sincronismo.',
    created_at        timestamp              not null comment 'Data em que o evento foi persistido. Não é a data exata de alteração no Banco Central',
    event_on_bacen_at timestamp              not null comment 'É a data em que o evento aconteceu no Bacen. Essa data é controlada no Banco Central e é a data utilizada para o sincronismo por eventos.',
    type              character varying(20)  not null comment 'Enum com o tipo de operação realizada: ADDED, REMOVED'
);
alter table content_identifier_event
    comment 'Armazena todos os eventos de manipulação do cadastro de chaves, gerando um cid para ser utilizado na sincronização de base';


create table sync_verifier_historic_action
(
    id                        integer auto_increment not null primary key comment 'Chave primária sequencial',
    id_sync_verifier_historic integer                not null comment 'O CID é o content identifier. É a representação hexadecimal, em lowercase, do resultado da função hmacSha256 de determinados campos da chave para manter o sincronismo.',
    cid                       character varying(64)  not null comment 'Indica qual o histórico de sincronismo que deu origem a esta ação',
    action                    character varying(20)  not null comment 'Enum com o tipo de ação corretiva: ADDED, REMOVED',
    constraint fk_sync_verifier foreign key (id_sync_verifier_historic) references sync_verifier_historic (id)
);
alter table sync_verifier_historic_action
    comment 'Armazena todas ações que devem ser feitas para correção dos dados para sincronismo da base';