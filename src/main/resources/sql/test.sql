

/*
DELETE FROM probe_config;
DELETE FROM probe_response;
SELECT * FROM probe_type;
SELECT count(*) FROM probe_response;

select
    c.custom_configuration,
    c.id,
	t.name,
	r.success,
	count(*)
from
	probe_response r
    join probe_config  c on r.probe_config = c.id
    join probe_type  t on c.probe_type = t.id 
group by
    custom_configuration,
    c.id,
	t.name,
	r.success
ORDER BY
	c.custom_configuration

SELECT * FROM probe_response where probe_config = 23726

*/