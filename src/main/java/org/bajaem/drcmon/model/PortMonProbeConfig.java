package org.bajaem.drcmon.model;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.mq.MessageSender;

public class PortMonProbeConfig extends HostProbeConfig
{

    public PortMonProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache, final MessageSender _sender)
    {
        super(_config, _cache, _sender);
    }

    private static final String PORT_KEY = "PORT";

    public Integer getPort()
    {
        return Integer.parseInt(getConfig().getCustomConfiguration().get(PORT_KEY));
    }

    public void setPort(final Integer port)
    {
        getConfig().getCustomConfiguration().put(PORT_KEY, port.toString());
    }
}