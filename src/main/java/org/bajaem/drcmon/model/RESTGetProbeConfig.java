package org.bajaem.drcmon.model;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;

public class RESTGetProbeConfig extends URLBasedConfig
{

    public RESTGetProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache)
    {
        super(_config, _cache);
    }
}