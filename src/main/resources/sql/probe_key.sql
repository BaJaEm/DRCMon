-- DROP TABLE probe_key

CREATE TABLE probe_key
(
	id				BIGINT PRIMARY KEY,
	name            VARCHAR(50) NOT NULL,
	user_id         VARCHAR(255) NOT NULL,
	secret          VARCHAR(2048) NOT NULL
);

CREATE UNIQUE INDEX uq_probe_key_name ON probe_key ( name ) ;