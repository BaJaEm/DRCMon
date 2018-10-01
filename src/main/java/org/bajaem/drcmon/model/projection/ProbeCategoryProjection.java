
package org.bajaem.drcmon.model.projection;

import org.bajaem.drcmon.model.ProbeCategory;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "full", types = { ProbeCategory.class })
public interface ProbeCategoryProjection
{
    public long getId();

    public String getName();

    public String getChannel();
}
