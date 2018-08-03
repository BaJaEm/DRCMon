package org.bajaem.netmon.drcmon.respository;

import org.bajaem.netmon.drcmon.model.ProbeType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProbeTypeRepository extends CrudRepository<ProbeType, Long>
{
    public ProbeType findByName(final String name);
}