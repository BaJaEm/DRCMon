CREATE TABLE probe_type
(
	id				INT PRIMARY KEY,
	name			VARCHAR(50) NOT NULL,
	description     VARCHAR(255) NOT NULL
);

ALTER TABLE probe_type ADD CONSTRAINT  probe_type_uq_name UNIQUE (name);


INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'Ping', 'Ping using java isReachable method - may or maynot be ICMP');
INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'PortMon', 'TCP Port Monitor - Checks if port is reachable');
INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'SQLQueryProbe', 'SQL Query Probe - checks if a query returns correct value');
