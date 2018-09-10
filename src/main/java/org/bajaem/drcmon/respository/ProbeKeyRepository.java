package org.bajaem.drcmon.respository;

import org.bajaem.drcmon.model.ProbeKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource
public interface ProbeKeyRepository extends CrudRepository<ProbeKey, Long>
{

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends ProbeKey> S save(S _entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(ProbeKey aLong);  
}
