package org.bajaem.drcmon.model;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;

public class SQLQueryProbeConfig extends URLBasedConfig
{

    public SQLQueryProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache)
    {
        super(_config, _cache);
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