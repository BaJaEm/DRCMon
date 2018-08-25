-- DROP TABLE probe_config

CREATE OR REPLACE TABLE probe_config 
(
	id					  BIGINT PRIMARY KEY,
	artifact_id			  VARCHAR(255) NULL,
	probe_type       	  VARCHAR(10) NOT NULL,
	polling_interval	  INT not null,
	delay_time			  INT,
	created_on			  datetime not null DEFAULT CURRENT_TIMESTAMP,
	created_by			  varchar(50) not null,
	last_modified_on      datetime not null  DEFAULT CURRENT_TIMESTAMP,
	last_modified_by      varchar(50) not null,
	enabled               CHAR(1) DEFAULT 'T' NOT NULL CHECK (enabled IN ('T', 'F')),
	custom_configuration  varchar(2048)
	
	
);

ALTER TABLE probe_config 
	ADD FOREIGN KEY ( probe_type )
	REFERENCES probe_type ( name );
	
