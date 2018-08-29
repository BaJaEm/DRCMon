-- DROP TABLE probe_type

CREATE TABLE probe_type
(
	id              BIGINT PRIMARY KEY,
	name			VARCHAR(10) NOT NULL UNIQUE,
	description     VARCHAR(255) NOT NULL
);


INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'Ping', 'Ping using java isReachable method - may or maynot be ICMP');
INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'PortMon', 'TCP Port Monitor - Checks if port is reachable');
INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'SQLQuery', 'SQL Query Probe - checks if a query returns correct value');
INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'RESTGet', 'Web Query Probe - checks if a REST GET returns correct value');

