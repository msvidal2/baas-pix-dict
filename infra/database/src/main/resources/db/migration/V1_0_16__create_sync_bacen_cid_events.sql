create table sync_bacen_cid_events
(
    key_type             character varying(20) not null comment 'O keyType é o tipo da chave utilizada no Pix. Novos tipos poderão surgir. Os valores atuais são: "CPF", "CNPJ", "PHONE", "EMAIL", "EVP"',
    last_sync_with_bacen datetime(3)           not null comment 'É a data em que ocorreu o ultimo sincronismo de eventos de cid. A cada sincronização esta está é atualizada com a mais recente.'
);
alter table sync_bacen_cid_events
    comment 'Armazena apenas 5 linhas, uma para cada tipo de chave, contendo a data mais recente em que os eventos de cid foram sincronizados';

create table bacen_cid_events
(
    id                integer auto_increment not null primary key comment 'Chave primária sequencial',
    key_type          character varying(20)  not null comment 'O keyType é o tipo da chave utilizada no Pix. Novos tipos poderão surgir. Os valores atuais são: "CPF", "CNPJ", "PHONE", "EMAIL", "EVP"',
    cid               character varying(64)  not null comment 'O CID é o content identifier. É a representação hexadecimal, em lowercase, do resultado da função hmacSha256 de determinados campos da chave para manter o sincronismo.',
    event_on_bacen_at datetime(3)            not null comment 'É a data em que o evento aconteceu no Bacen. Essa data é controlada no Banco Central e é a data utilizada para o sincronismo por eventos.',
    type              character varying(20)  not null comment 'Enum com o tipo de operação realizada: ADDED, REMOVED'
);
alter table bacen_cid_events
    comment 'Armazena todos os eventos de cids do cadastro de chaves. Estes dados são gerados do lado do bacen e são sincronizados aqui afim de executar o processo de reconciliação de base de dados.';
