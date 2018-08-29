
package org.bajaem.drcmon.respository;

import org.bajaem.drcmon.model.ProbeType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource
public interface ProbeTypeRepository extends CrudRepository<ProbeType, Long>
{

    public ProbeType findByName(final String name);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends ProbeType> S save(S _entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(ProbeType aString);
}
