
CREATE TABLE public.pix_key
(
    key character varying(100)  NOT NULL,
    "creationDate" timestamp without time zone NOT NULL,
    "updateDate" timestamp without time zone,
    type character varying(10)  NOT NULL,
    participant character varying(8)  NOT NULL,
    branch character varying(4)  NOT NULL,
    "accountNumber" character varying(20)  NOT NULL,
    "accountType" character varying(4)  NOT NULL,
    "ownerType" character varying(14)  NOT NULL,
    "taxId" character varying(14)  NOT NULL,
    name character varying(100)  NOT NULL,
    reason character varying(14)  NOT NULL,
    CONSTRAINT pix_key_pkey PRIMARY KEY (key)
);