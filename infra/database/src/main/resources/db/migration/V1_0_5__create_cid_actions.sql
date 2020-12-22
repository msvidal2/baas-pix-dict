CREATE TABLE IF NOT EXISTS content_identifier_actions
(
    id serial NOT NULL,
    id_content_identifier_file integer NULL,
    id_vsync_history integer NULL,
    creation_date timestamp NOT NULL,
    content json not null ,
    action varchar(10) not null ,
    cid varchar(100) NOT NULL,
    CONSTRAINT content_identifier_actions_pkey PRIMARY KEY (id)
);

