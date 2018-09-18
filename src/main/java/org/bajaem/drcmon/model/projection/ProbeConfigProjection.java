
package org.bajaem.drcmon.model.projection;

import java.sql.Timestamp;
import java.util.Map;

import org.bajaem.drcmon.model.ProbeCategory;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.ProbeKey;
import org.bajaem.drcmon.model.ProbeType;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "full", types = { ProbeConfig.class })
public interface ProbeConfigProjection
{

    public long getId();

    public String getArtifactId();

    public int getPollingInterval();

    public int getDelayTime();

    public Timestamp getCreatedOn();

    public String getCreatedBy();

    public Timestamp getLastModifiedOn();

    public String getLastModifiedBy();

    public boolean isEnabled();

    public ProbeType getProbeType();

    public ProbeKey getProbeKey();

    public ProbeCategory getProbeCategory();

    public Map<String, String> getCustomConfiguration();
}
