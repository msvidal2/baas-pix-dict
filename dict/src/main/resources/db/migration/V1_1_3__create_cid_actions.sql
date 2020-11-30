CREATE TABLE public.content_identifier_actions
(
    id serial NOT NULL,
    id_content_identifier_file integer NULL,
    id_vsync_history integer NULL,
    creation_date timestamp without time zone NOT NULL,
    content json not null ,
    action character varying(10) not null ,
    cid character varying(100) NOT NULL,
    CONSTRAINT content_identifier_actions_pkey PRIMARY KEY (id)
);

