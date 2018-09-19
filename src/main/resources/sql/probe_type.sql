-- DROP TABLE probe_type

CREATE TABLE probe_type
(
	id              BIGINT PRIMARY KEY,
	name			VARCHAR(10) NOT NULL UNIQUE,
	description     VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX probe_type_uq_name ON  probe_type (name);

INSERT INTO probe_type (id, name, description) VALUES (nextval('key_seq'), 'Ping', 'Ping using java isReachable method - may or may not be ICMP');
INSERT INTO probe_type (id, name, description) VALUES (nextval('key_seq'), 'PortMon', 'TCP Port Monitor - Checks if port is reachable');
INSERT INTO probe_type (id, name, description) VALUES (nextval('key_seq'), 'SQLQuery', 'SQL Query Probe - checks if a query returns correct value');
INSERT INTO probe_type (id, name, description) VALUES (nextval('key_seq'), 'RESTGet', 'Web Query Probe - checks if a REST GET returns correct value');
INSERT INTO probe_type (id, name, description) VALUES (nextval('key_seq'), 'WebBot', 'selienium web driver probe');
