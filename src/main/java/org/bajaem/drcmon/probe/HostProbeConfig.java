package org.bajaem.drcmon.probe;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.mq.MessageSender;

public abstract class HostProbeConfig extends Configurable
{

    public HostProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache, final MessageSender _sender)
    {
        super(_config, _cache, _sender);
    }

    public static final String HOST_KEY = "HOST";

    public String getHost()
    {
        return getConfig().getCustomConfiguration().get(HOST_KEY);
    }

    public void setHost(final String host)
    {
        getConfig().getCustomConfiguration().put(HOST_KEY, host);
    }
}