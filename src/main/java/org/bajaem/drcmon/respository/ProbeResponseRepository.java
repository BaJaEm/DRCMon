
package org.bajaem.drcmon.respository;

import org.bajaem.drcmon.model.ProbeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource
public interface ProbeResponseRepository extends PagingAndSortingRepository<ProbeResponse, Long>
{

    @RestResource
    public Page<ProbeResponse> findByProbeConfig_Id(@Param(value = "id") final Long id, final Pageable p);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends ProbeResponse> S save(S _entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(ProbeResponse aLong);
}
