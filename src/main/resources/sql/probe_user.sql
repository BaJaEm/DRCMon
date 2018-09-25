-- DROP TABLE probe_user

CREATE TABLE probe_user
(
	id				BIGINT PRIMARY KEY,
	name            VARCHAR(50) NOT NULL,
	user_id         VARCHAR(255) NOT NULL,
	secret          VARCHAR(2048) NOT NULL,
	admin           CHAR(1) CHECK (admin IN ('T', 'F'))
);

CREATE UNIQUE INDEX uq_probe_user_name ON probe_user ( name ) ;
CREATE UNIQUE INDEX uq_probe_user_user_id ON probe_user ( user_id ) ;