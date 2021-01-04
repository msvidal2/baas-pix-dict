alter table pix_key add column donated_automatically boolean default false;

alter table pix_key add column completion_period_end timestamp;