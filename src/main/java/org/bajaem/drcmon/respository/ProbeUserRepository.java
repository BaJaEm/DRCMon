
package org.bajaem.drcmon.respository;

import org.bajaem.drcmon.model.ProbeUser;
import org.bajaem.drcmon.model.projection.ProbeUserProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource(excerptProjection = ProbeUserProjection.class)
public interface ProbeUserRepository extends CrudRepository<ProbeUser, Long>
{

    public ProbeUser findByName(final String name);

    public ProbeUser findByUserId(final String userId);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends ProbeUser> S save(S _entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(ProbeUser aLong);
}
