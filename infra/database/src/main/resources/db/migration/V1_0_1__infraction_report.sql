CREATE TABLE dict.infraction_report (
	infraction_report_id varchar(36) NOT NULL,
	end_to_end_id varchar(32) NOT NULL,
	reported_by varchar(32) NOT NULL,
	situation varchar(32) NOT NULL,
	ispb_debited varchar(8) NOT NULL,
	ispb_credited varchar(8) NOT NULL,
	created_date timestamp NOT NULL,
	last_updated_date timestamp NOT NULL,
	infraction_type varchar(10) NOT NULL,
	details varchar(2000) NULL,
	analyze_result varchar(10) NULL,
	analyze_details varchar(2000) NULL,
    CONSTRAINT infraction_report_id_primary_key PRIMARY KEY (infraction_report_id)
);

CREATE INDEX infraction_report_e2e ON dict.infraction_report (end_to_end_id);