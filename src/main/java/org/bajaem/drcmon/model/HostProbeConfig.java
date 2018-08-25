package org.bajaem.drcmon.model;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class HostProbeConfig extends ProbeConfig
{

    private static final String HOST_KEY = "HOST";

    @Transient
    @JsonIgnore
    public String getHost()
    {
        return getCustomConfiguration().get(HOST_KEY);
    }

    public void setHost(final String host)
    {
        getCustomConfiguration().put(HOST_KEY, host);
    }
}
