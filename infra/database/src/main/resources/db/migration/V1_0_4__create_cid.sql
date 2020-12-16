CREATE TABLE IF NOT EXISTS content_identifier
(
    cid varchar(100) NOT NULL,
    pix_key varchar(100) NOT NULL,
    key_type varchar(20) NOT NULL,
    creation_date timestamp NOT NULL,
    CONSTRAINT content_identifier_pkey PRIMARY KEY (cid),
    FOREIGN KEY (pix_key,key_type) REFERENCES pix_key(pix_key,type)
);

CREATE TABLE IF NOT EXISTS content_identifier_file
(
    id integer NOT NULL,
    status varchar(20) NOT NULL,
    key_type varchar(20) NOT NULL,
    request_date timestamp NOT NULL,
    url varchar(300) NULL,
    length bigint NULL,
    sha256 varchar(300) NULL,
    CONSTRAINT content_identifier_file_pkey PRIMARY KEY (id)
);
