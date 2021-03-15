drop table dict.content_identifier_actions;

alter table dict.sync_verifier_historic_action
    add content json not null;

alter table dict.sync_verifier_historic_action
    add creation_date datetime(3) not null default now(3);

alter table dict.sync_verifier_historic
    add reconciliation_method varchar(100) null;