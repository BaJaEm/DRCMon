
package org.bajaem.drcmon.model.projection;

import org.bajaem.drcmon.model.ProbeKey;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "full", types = { ProbeKey.class })
public interface ProbeKeyProjection
{

    public long getId();

    public String getName();

    public String getUserId();
}
