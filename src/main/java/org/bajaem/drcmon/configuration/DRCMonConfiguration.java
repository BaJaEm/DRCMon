
package org.bajaem.drcmon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/drcmon.properties")
public class DRCMonConfiguration
{

    private final int poolSize;

    private final int refreshTime;

    @Autowired
    public DRCMonConfiguration(@Value("${poolSize}") final int _poolsize,
            @Value("${refreshTime}") final int _refreshTime)
    {
        poolSize = _poolsize;
        refreshTime = _refreshTime;
    }

    public int getPoolSize()
    {
        return poolSize;
    }

    public int getRefreshTime()
    {
        return refreshTime;
    }
}