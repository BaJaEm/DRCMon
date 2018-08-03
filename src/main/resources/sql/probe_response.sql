

CREATE OR REPLACE TABLE probe_response
(
	id				BIGINT PRIMARY KEY,
	probe_config	BIGINT NOT NULL,
	start_time		DATETIME NOT NULL,
	end_time        DATETIME NOT NULL,
	success         CHAR(1) CHECK (success IN ('T', 'F')),
	error           VARCHAR(255),
	error_message    VARCHAR(255)
	
);

ALTER TABLE probe_response 
	ADD FOREIGN KEY ( probe_config )
	REFERENCES probe_config ( id );
	
