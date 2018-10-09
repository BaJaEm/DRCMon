package org.bajaem.drcmon.probe;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.mq.MessageSender;

public abstract class Configurable
{
    private final ProbeConfig config;

    private final ProbeMarkerCache cache;

    private final MessageSender sender;

    public Configurable(final ProbeConfig _config, final ProbeMarkerCache _cache, final MessageSender _sender)
    {
        config = _config;
        cache = _cache;
        config.setProbeType(cache.getProbeTypeByConfig(this.getClass()));
        sender = _sender;
    }

    public ProbeConfig getConfig()
    {
        return config;
    }

    public ProbeMarkerCache getProbeMarkerCache()
    {
        return cache;
    }

    public MessageSender getSender()
    {
        return sender;
    }
}