-- pixkey --
ALTER TABLE pix_key_event DROP FOREIGN KEY pix_key_fk;

ALTER TABLE pix_key_event change pix_key_fk pix_key varchar(100) NOT NULL;
ALTER TABLE pix_key_event change key_type_fk pix_key_type varchar(10) NOT NULL;

ALTER TABLE pix_key_event add column request_identifier varchar(36);


-- claim --
ALTER TABLE claim_event DROP FOREIGN KEY claim_fk;

ALTER TABLE claim_event add column request_identifier varchar(36);