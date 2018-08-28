package org.bajaem.drcmon.model;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;

public abstract class HostProbeConfig extends Configurable
{

    public HostProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache)
    {
        super(_config, _cache);
    }

    private static final String HOST_KEY = "HOST";

    public String getHost()
    {
        return getConfig().getCustomConfiguration().get(HOST_KEY);
    }

    public void setHost(final String host)
    {
        getConfig().getCustomConfiguration().put(HOST_KEY, host);
    }
}