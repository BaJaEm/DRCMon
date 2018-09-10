
package org.bajaem.drcmon.engine;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.DRCMonConfiguration;
import org.bajaem.drcmon.configuration.SystemUserWrapper;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.probe.Probe;
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

    private boolean running = false;

    private final Map<String, ScheduledExecutorService> pool = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ScheduledFuture<?>> probeMap = new ConcurrentHashMap<>();

    private final DRCMonConfiguration config;

    private final ProbeConfigurator probeConfig;

    private Thread engineThread;

    private Instant lastRefreshed;

    @Autowired
    public MonitorEngine(final DRCMonConfiguration _config, final ProbeConfigurator _probeConfig)
    {
        config = _config;
        probeConfig = _probeConfig;
    }

    public int getPoolSize()
    {
        return config.getPoolSize();
    }

    public DRCMonConfiguration getConfig()
    {
        return config;
    }

    public boolean isRunning()
    {
        return running;
    }

    private void init()
    {
        final Map<String, Probe> configMap = probeConfig.getProbes();

        for (final String key : configMap.keySet())
        {
            final Probe probe = configMap.get(key);
            final ScheduledFuture<?> future = getExecutor(probe).scheduleAtFixedRate(probe,
                    probe.getProbeConfig().getDelayTime(), probe.getProbeConfig().getPollingInterval(),
                    TimeUnit.SECONDS);
            probeMap.put(key, future);
            lastRefreshed = Instant.now();
        }
    }

    private ScheduledExecutorService getExecutor(final Probe probe)
    {
        final String key = probe.getProbeConfig().getProbeType().getName();
        final ScheduledExecutorService service = pool.get(key);
        if (service == null || service.isTerminated())
        {
            pool.put(key, Executors.newScheduledThreadPool(config.getPoolSize()));
        }
        return pool.get(key);

    }

    private void refresh()
    {
        LOG.info("Starting Refresh");
        // TODO: do we really need to get all of the configs from the DB each
        // time?
        final Map<String, Probe> dbMap = probeConfig.getProbes();

        final Set<String> removed = new HashSet<String>(probeMap.keySet());

        final HashSet<String> added = new HashSet<String>(dbMap.keySet());
        final Set<String> common = new HashSet<String>(dbMap.keySet());

        // added
        added.removeAll(probeMap.keySet());
        for (final String key : added)
        {
            final Probe probe = dbMap.get(key);
            if (probe.getProbeConfig().isEnabled())
            {
                final ScheduledFuture<?> future = getExecutor(probe).scheduleAtFixedRate(probe,
                        probe.getProbeConfig().getDelayTime(), probe.getProbeConfig().getPollingInterval(),
                        TimeUnit.SECONDS);
                probeMap.put(key, future);
            }
        }

        // removed - Generally this should not happen - we really only want to
        // disable configs - in order to preserve the history
        removed.removeAll(dbMap.keySet());
        for (final String key : removed)
        {
            final ScheduledFuture<?> future = probeMap.get(key);
            future.cancel(false);
            probeMap.remove(key);
        }
        // common
        common.addAll(probeMap.keySet());
        common.removeAll(added);
        common.removeAll(removed);
        for (final String key : common)
        {
            final Probe probe = dbMap.get(key);
            final Instant lm = probe.getProbeConfig().getLastModifiedOn().toInstant();
            if (lm.isAfter(lastRefreshed))
            {
                if (probe.getProbeConfig().isEnabled())
                {
                    LOG.info("Key: " + key + " - updated");
                    final ScheduledFuture<?> future = probeMap.get(key);
                    if (null != future)
                    {
                        future.cancel(false);
                    }
                    final ScheduledFuture<?> newFuture = getExecutor(probe).scheduleAtFixedRate(probe,
                            probe.getProbeConfig().getDelayTime(), probe.getProbeConfig().getPollingInterval(),
                            TimeUnit.SECONDS);
                    probeMap.put(key, newFuture);
                }
                else
                {
                    LOG.info("Key: " + key + " - removed");
                    final ScheduledFuture<?> future = probeMap.get(key);
                    if (null != future)
                    {
                        future.cancel(false);
                        probeMap.remove(key);
                    }
                }
            }
        }
        LOG.info("Completed Refresh");
    }

    public void start()
    {
        LOG.info("Starting: " + running + " : " + engineThread + " alive -> "
                + (null != engineThread ? engineThread.isAlive() : null));
        if (!isRunning())
        {
            if (null == engineThread || !engineThread.isAlive())
            {
                engineThread = new Thread(() -> SystemUserWrapper.executeAsSystem(() -> mainLoop()));
            }
            synchronized (engineThread)
            {
                engineThread.start();
            }
        }
        else
        {
            LOG.info(running + " : " + engineThread);
            LOG.info("Engine is already running");
        }
    }

    public void stop()
    {
        LOG.info("Stopping: " + running + " : " + engineThread + " alive -> "
                + (null != engineThread ? engineThread.isAlive() : null));
        if (isRunning())
        {
            LOG.info("Shutting down");
            running = false;
            synchronized (engineThread)
            {
                engineThread.notify();
            }
        }
        else
        {
            LOG.info(new Exception());
            LOG.info(running + " : " + engineThread);
            LOG.info("Engine is already stopped");
        }
    }

    private void shutdown()
    {
        try
        {
            for (final ScheduledExecutorService service : pool.values())
            {
                service.shutdown();
                service.awaitTermination(2, TimeUnit.MINUTES);
            }
        }
        catch (final InterruptedException e)
        {
            LOG.warn(e);
        }
        finally
        {
            running = false;
        }
    }

    private void mainLoop()
    {
        if (!isRunning())
        {
            running = true;
            LOG.info("Starting Monitor Engine: ");
            try
            {
                init();
                while (isRunning())
                {
                    try
                    {
                        synchronized (engineThread)
                        {
                            engineThread.wait(config.getRefreshTime() * 6000);
                            refresh();
                        }
                    }
                    catch (final InterruptedException e)
                    {
                        // do nothing
                    }
                }
                shutdown();
                LOG.info("Engine shut down complete");
            }
            catch (final DRCStartupException e)
            {
                running = false;
            }
        }
        else
        {
            LOG.info("Engine is already running");
        }
    }
}
