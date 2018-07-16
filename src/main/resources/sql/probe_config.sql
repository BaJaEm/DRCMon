

CREATE OR REPLACE TABLE probe_config 
(
	id					BIGINT PRIMARY KEY,
	artifact_id			VARCHAR(255) NULL,
	monitor_type_id		INT NOT NULL,
	host				VARCHAR(255) NOT NULL,
	port				int,
	protocol			VARCHAR(10),
	polling_interval	INT not null,
	delay_time			INT,
	created_on			datetime not null,
	created_by			varchar(50) not null,
	last_modified_on    datetime not null,
	last_modified_by    varchar(50) not null
	
	
);