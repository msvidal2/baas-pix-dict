CREATE TABLE public.claim
(
    id character varying(100) NOT NULL,
    type character varying(20) NOT NULL,
    "key" character varying(100) NOT NULL,
    key_type character varying(20) NOT NULL,
    claimer_participant integer NOT NULL,
    claimer_branch character varying(4),
    claimer_account_number character varying(20) NOT NULL,
    claimer_account_type character varying(20) NOT NULL,
    claimer_account_opening_date timestamp without time zone NOT NULL,
    claimer_type character varying(20) NOT NULL,
    claimer_tax_id character varying(14) NOT NULL,
    claimer_name character varying(100),
    donor_participant integer,
    status character varying(30),
    completion_period_end timestamp without time zone,
    resolution_period_end timestamp without time zone,
    last_modified timestamp without time zone,
    creation_date timestamp without time zone,
    update_date timestamp without time zone,
    CONSTRAINT claim_pkey PRIMARY KEY (id)
);