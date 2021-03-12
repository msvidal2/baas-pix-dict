
CREATE INDEX pix_key_event_creation_date ON pix_key_event (creation_date DESC);
CREATE INDEX pix_key_event_pix_key ON pix_key_event (pix_key DESC, pix_key_type DESC);
CREATE INDEX pix_key_event_request_identifier ON pix_key_event (request_identifier DESC);
CREATE INDEX pix_key_event_key_identifier ON pix_key_event (pix_key DESC, pix_key_type DESC, request_identifier DESC);
