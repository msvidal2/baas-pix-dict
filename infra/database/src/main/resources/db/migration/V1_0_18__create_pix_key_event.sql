CREATE TABLE IF NOT EXISTS pix_key_event
(
    id integer(100) NOT NULL,
    type varchar(20) NOT NULL,
    pix_key_fk varchar(100) NOT NULL,
    key_type_fk varchar(10) NOT NULL,
    event_data text NOT NULL,
    creation_date timestamp,
    CONSTRAINT pix_key_event_pkey PRIMARY KEY (id),
    CONSTRAINT pix_key_fk FOREIGN KEY (pix_key_fk, key_type_fk) REFERENCES pix_key(pix_key, type)
);