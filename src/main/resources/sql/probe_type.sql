CREATE TABLE probe_type
(
	id				INT PRIMARY KEY,
	name			VARCHAR(50) NOT NULL,
	description     VARCHAR(255) NOT NULL
);
-- TODO make name unique
INSERT INTO probe_type (id, name, description) VALUES (key_seq.nextval, 'Ping', 'Ping using java isReachable method - may or maynot be ICMP');