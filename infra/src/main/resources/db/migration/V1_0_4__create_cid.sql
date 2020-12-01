CREATE TABLE dict.content_identifier
(
    cid character varying(100) NOT NULL,
    key character varying(100) NOT NULL,
    key_type character varying(20) NOT NULL,
    creation_date timestamp without time zone NOT NULL,
    CONSTRAINT content_identifier_pkey PRIMARY KEY (cid),
    FOREIGN KEY (key,key_type) REFERENCES dict.pix_key(key,type)
);

CREATE TABLE dict.content_identifier_file
(
    id integer NOT NULL,
    status character varying(20) NOT NULL,
    key_type character varying(20) NOT NULL,
    request_date timestamp without time zone NOT NULL,
    url character varying(300) NULL,
    length bigint NULL,
    sha256 character varying(300) NULL,
    CONSTRAINT content_identifier_file_pkey PRIMARY KEY (id)
);
