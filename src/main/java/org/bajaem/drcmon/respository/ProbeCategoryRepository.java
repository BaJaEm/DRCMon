
package org.bajaem.drcmon.respository;

import org.bajaem.drcmon.model.ProbeCategory;
import org.bajaem.drcmon.model.projection.ProbeCategoryProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource(excerptProjection = ProbeCategoryProjection.class)
public interface ProbeCategoryRepository extends CrudRepository<ProbeCategory, Long>
{

    public ProbeCategory findByName(final String name);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends ProbeCategory> S save(S _entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(ProbeCategory aLong);
}
