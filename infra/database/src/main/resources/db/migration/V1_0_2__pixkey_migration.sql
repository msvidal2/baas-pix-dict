CREATE TABLE IF NOT EXISTS pix_key
(
    pix_key varchar(100)  NOT NULL,
    type varchar(10)  NOT NULL,
    participant integer NOT NULL,
    branch varchar(4) NULL,
    account_number varchar(20) NOT NULL,
    account_type varchar(10) NOT NULL,
    opening_date timestamp NOT NULL,
    person_type varchar(20) NOT NULL,
    tax_id varchar(14) NOT NULL,
    name varchar(100) NOT NULL,
    reason varchar(20) NOT NULL,
    correlation_id varchar(32) NOT NULL,
    creation_date timestamp NOT NULL,
    ownership_date timestamp NOT NULL,
    update_date timestamp ,
    open_claim_creation_date timestamp ,
    request_id varchar(36) NOT NULL,
    CONSTRAINT pix_key_primary_key PRIMARY KEY (pix_key, type)
);

CREATE INDEX pix_key_key ON pix_key (pix_key);
CREATE INDEX pix_key_key_type ON pix_key (pix_key, type);
CREATE INDEX pix_key_taxId ON pix_key (tax_id);
CREATE INDEX pix_key_account ON pix_key (participant, branch, account_number, account_type);
