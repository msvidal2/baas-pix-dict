
CREATE TABLE dict.config (
    key character varying(250) NOT NULL,
    value character varying(250) NOT NULL,
    CONSTRAINT config_pkey PRIMARY KEY (key)
);