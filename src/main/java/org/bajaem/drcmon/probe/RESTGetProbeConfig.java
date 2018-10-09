package org.bajaem.drcmon.probe;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.mq.MessageSender;

public class RESTGetProbeConfig extends URLBasedConfig
{

    public RESTGetProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache, final MessageSender _sender)
    {
        super(_config, _cache, _sender);
    }
}