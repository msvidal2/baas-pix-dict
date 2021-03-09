CREATE TABLE IF NOT EXISTS claim_event
(
    id integer(100) NOT NULL,
    type varchar(20) NOT NULL,
    claim_id varchar(100) NOT NULL,
    event_data text NOT NULL,
    creation_date timestamp,
    CONSTRAINT claim_event_pkey PRIMARY KEY (id),
    CONSTRAINT claim_fk FOREIGN KEY (claim_id) REFERENCES claim(id)
);