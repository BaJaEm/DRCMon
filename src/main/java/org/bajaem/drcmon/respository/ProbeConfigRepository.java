
package org.bajaem.drcmon.respository;

import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.projection.ProbeConfigProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource(excerptProjection = ProbeConfigProjection.class)
public interface ProbeConfigRepository extends CrudRepository<ProbeConfig, Long>
{

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends ProbeConfig> S save(S _entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(ProbeConfig aLong);

}
