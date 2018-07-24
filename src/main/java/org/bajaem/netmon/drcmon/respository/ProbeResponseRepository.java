package org.bajaem.netmon.drcmon.respository;

import org.bajaem.netmon.drcmon.model.ProbeResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProbeResponseRepository extends CrudRepository<ProbeResponse, Long>
{
}
