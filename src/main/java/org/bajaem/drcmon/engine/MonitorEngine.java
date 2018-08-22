
package org.bajaem.drcmon.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.DRCMonConfiguration;
import org.bajaem.drcmon.probe.Probe;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.bajaem.drcmon.respository.ProbeResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Engine to populate probe pool, run Probes, coordinate Responses and ensure
 * that probes are running.
 *
 */
@Component
public class MonitorEngine
{
    private static final Logger LOG = LogManager.getLogger();

    // TODO: separate pool for each probe type
    private final ScheduledExecutorService pool;

    private final ConcurrentHashMap<String, ScheduledFuture<?>> probeMap = new ConcurrentHashMap<>();

    private final DRCMonConfiguration config;

    private final ProbeConfigurator probeConfig;



    @Autowired
    public MonitorEngine(final DRCMonConfiguration _config, final ProbeConfigurator _probeConfig)
    {
        config = _config;
        probeConfig = _probeConfig;
        pool = Executors.newScheduledThreadPool(config.getPoolSize());
    }

    public void init()
    {
        final Map<String, Probe> configMap = probeConfig.getProbes();
        for (final String key : configMap.keySet())
        {
            final Probe probe = configMap.get(key);
            final ScheduledFuture<?> future = pool.scheduleAtFixedRate(probe, probe.getProbeConfig().getDelayTime(),
                    probe.getProbeConfig().getPollingInterval(), TimeUnit.SECONDS);
            probeMap.put(key, future);
        }
    }

    public int getPoolSize()
    {
        return config.getPoolSize();
    }

    public DRCMonConfiguration getConfig()
    {
        return config;
    }
}
