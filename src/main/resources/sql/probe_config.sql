-- DROP TABLE probe_config

CREATE TABLE probe_config 
(
	id					  BIGINT PRIMARY KEY,
	artifact_id			  VARCHAR(255) NULL,
	probe_type       	  BIGINT NOT NULL,
	polling_interval	  INT not null,
	delay_time			  INT,
	created_on			  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	created_by			  VARCHAR(50) NOT NULL,
	last_modified_on      TIMESTAMP NOT NULL  DEFAULT CURRENT_TIMESTAMP,
	last_modified_by      VARCHAR(50) not null,
	enabled               CHAR(1) DEFAULT 'T' NOT NULL CHECK (enabled IN ('T', 'F')),
	probe_key             BIGINT,
	custom_configuration  VARCHAR(2048)
);


ALTER TABLE probe_config 
	ADD FOREIGN KEY ( probe_type )
	REFERENCES probe_type ( id );
	
ALTER TABLE probe_config
	ADD FOREIGN KEY ( probe_key )
	REFERENCES probe_key ( id );
