CREATE TABLE IF NOT EXISTS job_execution  (
	id serial NOT NULL,
	start_time TIMESTAMP DEFAULT NULL,
	end_time TIMESTAMP DEFAULT NULL,
	type  VARCHAR(100),
	exit_message VARCHAR(2500),
	CONSTRAINT job_execution_primary_key PRIMARY KEY (id)
);
