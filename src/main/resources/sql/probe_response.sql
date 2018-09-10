-- DROP TABLE probe_response

CREATE TABLE probe_response
(
	id				BIGINT PRIMARY KEY,
	probe_config	BIGINT NOT NULL,
	start_time		TIMESTAMP NOT NULL,
	end_time        TIMESTAMP NOT NULL,
	success         CHAR(1) CHECK (success IN ('T', 'F')),
	error           VARCHAR(255),
	error_message   VARCHAR(2048)
	
);

ALTER TABLE probe_response 
	ADD FOREIGN KEY ( probe_config )
	REFERENCES probe_config ( id );
	
