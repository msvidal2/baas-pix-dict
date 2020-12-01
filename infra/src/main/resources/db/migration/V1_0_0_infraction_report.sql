CREATE TABLE dict.infraction_report (
	infraction_report_id varchar(36) NOT NULL,
	end_to_end_id varchar(32) NOT NULL,
	reported_by numeric NOT NULL,
	situation int NOT NULL,
	ispb_debited numeric NOT NULL,
	ispb_credited numeric NOT NULL,
	created_date timestamptz NOT NULL,
	last_updated_date timestamptz NOT NULL,
	ispb_requester numeric NULL,
	infraction_type int NOT NULL,
	details varchar(2000) NULL,
	request_identifier varchar(36) NOT NULL,
	analyze_result int null,
	analyze_details varchar(2000) NULL
);

CREATE INDEX infraction_report_e2e ON dict.infraction_report (end_to_end_id);