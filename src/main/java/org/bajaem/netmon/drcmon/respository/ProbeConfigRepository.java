
package org.bajaem.netmon.drcmon.respository;

import org.bajaem.netmon.drcmon.model.ProbeConfig;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource
public interface ProbeConfigRepository extends PagingAndSortingRepository<ProbeConfig, Long>
{

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends ProbeConfig> S save(S _entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(ProbeConfig aLong);

}
