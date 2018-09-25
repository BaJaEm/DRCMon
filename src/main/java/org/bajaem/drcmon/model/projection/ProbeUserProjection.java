
package org.bajaem.drcmon.model.projection;

import org.bajaem.drcmon.model.ProbeUser;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "full", types = { ProbeUser.class })
public interface ProbeUserProjection
{

    public long getId();

    public String getName();

    public String getUserId();

    public boolean isAdmin();
}
