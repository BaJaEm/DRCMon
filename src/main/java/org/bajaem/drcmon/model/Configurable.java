package org.bajaem.drcmon.model;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;

public abstract class Configurable
{
    private final ProbeConfig config;

    private final ProbeMarkerCache cache;

    public Configurable(final ProbeConfig _config, final ProbeMarkerCache _cache)
    {
        config = _config;
        cache = _cache;
        config.setProbeType(cache.getProbeTypeByConfig(this.getClass()));
    }

    public ProbeConfig getConfig()
    {
        return config;
    }

    public ProbeMarkerCache getProbeMarkerCache()
    {
        return cache;
    }
}