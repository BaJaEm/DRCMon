package org.bajaem.drcmon.model;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.mq.MessageSender;

public class SQLQueryProbeConfig extends URLBasedConfig
{

    public SQLQueryProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache, final MessageSender _sender)
    {
        super(_config, _cache, _sender);
    }

    private static final String QUERY_KEY = "QUERY";

    public String getQuery()
    {
        return getConfig().getCustomConfiguration().get(QUERY_KEY);
    }

    public void setQuery(final String _query)
    {
        getConfig().getCustomConfiguration().put(QUERY_KEY, _query);
    }
}