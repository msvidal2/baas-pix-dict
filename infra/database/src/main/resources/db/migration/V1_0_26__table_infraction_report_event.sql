
CREATE TABLE IF NOT EXISTS infraction_report_event
(
    id                   bigint(100) NOT NULL AUTO_INCREMENT,
    type                 varchar(30) NOT NULL,
    infraction_report_id varchar(100),
    event_data           text        NOT NULL,
    creation_date        timestamp   NOT NULL,
    request_identifier   VARCHAR(36) NOT NULL,
    CONSTRAINT infraction_report_event_pkey PRIMARY KEY (id)
);

CREATE INDEX infraction_event_creation_date ON infraction_report_event (creation_date DESC);
CREATE INDEX infraction_event_infraction_id ON infraction_report_event (infraction_report_id DESC);
CREATE INDEX infraction_event_request_identifier ON infraction_report_event (request_identifier DESC);
CREATE INDEX infraction_event_identifier_by_type ON infraction_report_event (type DESC, request_identifier DESC);
