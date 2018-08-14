
SELECT key_seq.nextval;

DROP SEQUENCE key_seq;

SELECT * FROM probe_response where start_time > '2018-08-05 13:00:00';
SELECT count(*) FROM probe_response
SELECT c.host, count(*) FROM probe_response AS r join probe_config AS c on r.probe_config = c.id group by c.host

SELECT * FROM probe_config;

SELECT * FROM PROBE_TYPE;


ALTER TABLE probe_config ALTER COLUMN host SET NULL;

DROP TABLE probe_response;
DROP TABLE probe_config;