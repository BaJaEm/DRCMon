package org.bajaem.netmon.drcmon.respository;

import org.bajaem.netmon.drcmon.model.ProbeConfig;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProbeConfigRepository extends PagingAndSortingRepository<ProbeConfig, Long> {

}
