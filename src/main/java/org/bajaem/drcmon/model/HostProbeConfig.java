package org.bajaem.drcmon.model;

import javax.persistence.Transient;

public abstract class HostProbeConfig extends ProbeConfig
{

    private static final String HOST_KEY = "HOST";

    @Transient
    public String getHost()
    {
        return getCustomConfiguration().get(HOST_KEY);
    }

    public void setHost(final String host)
    {
        getCustomConfiguration().put(HOST_KEY, host);
    }
}
