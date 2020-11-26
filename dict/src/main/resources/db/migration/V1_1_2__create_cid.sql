CREATE TABLE public.content_identifier
(
    cid character varying(100) NOT NULL,
    type character varying(20) NOT NULL,
    key_type character varying(20) NOT NULL,
    CONSTRAINT content_identifier_pkey PRIMARY KEY (cid)
);

CREATE TABLE public.content_identifier_file
(
    id integer NOT NULL,
    status character varying(20) NOT NULL,
    key_type character varying(20) NOT NULL,
    request_date timestamp without time zone NOT NULL,
    creation_date timestamp without time zone NOT NULL,
    url character varying(300) NOT NULL,
    lenght bigint NOT NULL,
    sha256 character varying(20) NOT NULL,
    CONSTRAINT content_identifier_file_pkey PRIMARY KEY (id)
);
