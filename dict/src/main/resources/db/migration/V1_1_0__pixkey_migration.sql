
CREATE TABLE public.pix_key
(
    key character varying(100)  NOT NULL,
    type character varying(10)  NOT NULL,
    participant integer NOT NULL,
    branch character varying(4) NULL,
    account_number character varying(20) NOT NULL,
    account_type character varying(10) NOT NULL,
    opening_date timestamp without time zone NOT NULL,
    person_type character varying(20) NOT NULL,
    tax_id character varying(14) NOT NULL,
    name character varying(100) NOT NULL,
    reason character varying(20) NOT NULL,
    correlation_id character varying(32) NOT NULL,
    creation_date timestamp without time zone NOT NULL,
    ownership_date timestamp without time zone NOT NULL,
    update_date timestamp without time zone,
    open_claim_creation_date timestamp without time zone,
    CONSTRAINT pix_key_primary_key PRIMARY KEY (key, type, tax_id)
);

CREATE INDEX pix_key_key ON public.pix_key (key);
CREATE INDEX pix_key_key_type ON public.pix_key (key, type);
CREATE INDEX pix_key_taxId ON public.pix_key (tax_id);
CREATE INDEX pix_key_account ON public.pix_key (participant, branch, account_number, account_type);
