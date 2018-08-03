package org.bajaem.netmon.drcmon.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bajaem.netmon.drcmon.DRCMonConfiguration;
import org.bajaem.netmon.drcmon.probe.Probe;
import org.bajaem.netmon.drcmon.probe.ProbeConfigurator;
import org.bajaem.netmon.drcmon.respository.ProbeConfigRepository;
import org.bajaem.netmon.drcmon.respository.ProbeResponseRepository;
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
    private final ScheduledExecutorService pool;

    private final ConcurrentHashMap<String, ScheduledFuture<?>> probeMap = new ConcurrentHashMap<>();

    private final DRCMonConfiguration config;

    private final ProbeConfigurator probeConfig;

    private final ProbeResponseRepository responseRepo;

    private final ProbeConfigRepository configRepo;

    @Autowired
    public MonitorEngine(final DRCMonConfiguration _config, final ProbeConfigurator _probeConfig,
            final ProbeResponseRepository _responseRepo, final ProbeConfigRepository _configRepo)
    {
        config = _config;
        probeConfig = _probeConfig;
        responseRepo = _responseRepo;
        configRepo = _configRepo;
        pool = Executors.newScheduledThreadPool(config.getPoolSize());
    }

    public void init()
    {
        final Map<String, Probe> configMap = probeConfig.getProbes();
        for (final String key : configMap.keySet())
        {
            final Probe probe = configMap.get(key);
            final ScheduledFuture<?> future = pool.scheduleAtFixedRate(probe, probe.getDelay(),
                    probe.getPollingInterval(), TimeUnit.SECONDS);
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