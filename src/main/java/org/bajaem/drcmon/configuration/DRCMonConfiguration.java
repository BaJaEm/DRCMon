
package org.bajaem.drcmon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DRCMonConfiguration
{

    private final int poolSize;

    private final int refreshTime;

    private final boolean enableBroker;

    private final boolean autostartEngine;

    private final String url;

    @Autowired
    public DRCMonConfiguration(//
            @Value("${bajaem.drcmon.poolSize:10}") final int _poolsize, //
            @Value("${bajaem.drcmon.refreshTime:60000}") final int _refreshTime, //
            @Value("${bajaem.drcmon.tcp-broker-enabled:false}") final Boolean _enableBroker, //
            @Value("${bajaem.drcmon.autostart-engine:false}") final Boolean _autostartEngine, //
            @Value("${bajaem.drcmon.broker-url:<none>}") final String _url)
    {
        poolSize = _poolsize;
        refreshTime = _refreshTime;
        enableBroker = _enableBroker;
        autostartEngine = _autostartEngine;
        url = _url;
    }

    public int getPoolSize()
    {
        return poolSize;
    }

    public int getRefreshTime()
    {
        return refreshTime;
    }

    public String getBrokerURL()
    {
        return url;
    }

    public boolean isTCPBrokerEnabled()
    {
        return enableBroker;
    }

    public boolean autostartEngine()
    {
        return autostartEngine;
    }
}