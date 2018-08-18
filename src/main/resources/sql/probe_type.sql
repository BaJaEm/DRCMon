-- DROP TABLE probe_type

CREATE TABLE probe_type
(
	name			VARCHAR(10) PRIMARY KEY,
	description     VARCHAR(255) NOT NULL
);


INSERT INTO probe_type (name, description) VALUES ('Ping', 'Ping using java isReachable method - may or maynot be ICMP');
INSERT INTO probe_type (name, description) VALUES ('PortMon', 'TCP Port Monitor - Checks if port is reachable');
INSERT INTO probe_type (name, description) VALUES ('SQLQuery', 'SQL Query Probe - checks if a query returns correct value');
INSERT INTO probe_type (name, description) VALUES ('RESTGet', 'Web Query Probe - checks if a REST GET returns correct value');
