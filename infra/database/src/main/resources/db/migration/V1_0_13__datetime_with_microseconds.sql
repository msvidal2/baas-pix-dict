# claim
alter table claim modify completion_period_end datetime(3) null;
alter table claim modify resolution_period_end datetime(3) null;
alter table claim modify last_modified datetime(3) null;
alter table claim modify update_date datetime(3) null;

# content_identifier_actions
alter table content_identifier_actions modify creation_date datetime(3) not null;

# content_identifier_event
alter table content_identifier_event modify created_at datetime(3) not null comment 'Data em que o evento foi persistido. Não é a data exata de alteração no Banco Central';
alter table content_identifier_event modify event_on_bacen_at datetime(3) not null comment 'É a data em que o evento aconteceu no Bacen. Essa data é controlada no Banco Central e é a data utilizada para o sincronismo por eventos.';

# content_identifier_file
alter table content_identifier_file modify request_date datetime(3) not null;

# infraction_report
alter table infraction_report modify created_date datetime(3) not null;
alter table infraction_report modify last_updated_date datetime(3) not null;

# pix_key
alter table pix_key modify opening_date datetime(3) not null;
alter table pix_key modify creation_date datetime(3) not null;
alter table pix_key modify ownership_date datetime(3) not null;
alter table pix_key modify update_date datetime(3) null;
alter table pix_key modify open_claim_creation_date datetime(3) null;
alter table pix_key modify completion_period_end datetime(3) null;

# sync_verifier
alter table sync_verifier modify synchronized_at datetime(3) not null comment 'Esta é a data que foi feita o sincronismo com o Banco Central';
alter table sync_verifier modify updated_at datetime(3) not null comment 'Esta é a data local que o sincronismo rodou pela ultima vez';

# sync_verifier_historic
alter table sync_verifier_historic modify synchronized_start datetime(3) not null comment 'Data do ultimo sincronismo que teve sucesso. Utilizada para filtrar todos CIDs apos ela';
alter table sync_verifier_historic modify synchronized_end datetime(3) not null comment 'Data que terminou o processo de sincronismo';