-- DROP TABLE probe_category

CREATE TABLE probe_category
(
	id				BIGINT PRIMARY KEY,
	name            VARCHAR(50) NOT NULL,
	channel         VARCHAR(25) NOT NULL
);

CREATE UNIQUE INDEX uq_probe_category_name ON probe_category ( name );
CREATE UNIQUE INDEX uq_probe_category_channel ON probe_category ( channel );