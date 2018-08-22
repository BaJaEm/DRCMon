
package org.bajaem.drcmon.engine;

import java.sql.Timestamp;
import java.util.Calendar;
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

    // TODO: separate pool for each probe type
    private final ScheduledExecutorService pool;

    private final ConcurrentHashMap<String, ScheduledFuture<?>> probeMap = new ConcurrentHashMap<>();

    private final DRCMonConfiguration config;

    private final ProbeConfigurator probeConfig;

    private Thread engineThread;

    private Calendar lastRefreshed;

    @Autowired
    public MonitorEngine(final DRCMonConfiguration _config, final ProbeConfigurator _probeConfig)
    {
        config = _config;
        probeConfig = _probeConfig;
        pool = Executors.newScheduledThreadPool(config.getPoolSize());
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
            final ScheduledFuture<?> future = pool.scheduleAtFixedRate(probe, probe.getProbeConfig().getDelayTime(),
                    probe.getProbeConfig().getPollingInterval(), TimeUnit.SECONDS);
            probeMap.put(key, future);
            lastRefreshed = Calendar.getInstance();
        }
    }

    private void refresh()
    {
        // TODO: do we really need to get all of the configs from the DB each
        // time?
        final Map<String, Probe> configMap = probeConfig.getProbes();

        final Set<String> current = probeMap.keySet();
        final Set<String> removed = new HashSet<String>(probeMap.keySet());

        final Set<String> updated = configMap.keySet();
        final HashSet<String> added = new HashSet<String>(updated);
        final Set<String> common = new HashSet<String>(updated);

        // added
        added.removeAll(updated);
        for (final String key : added)
        {
            final Probe probe = configMap.get(key);
            final ScheduledFuture<?> future = pool.scheduleAtFixedRate(probe, probe.getProbeConfig().getDelayTime(),
                    probe.getProbeConfig().getPollingInterval(), TimeUnit.SECONDS);
            probeMap.put(key, future);
        }

        // removed
        removed.removeAll(current);
        for (final String key : removed)
        {
            final ScheduledFuture<?> future = probeMap.get(key);
            future.cancel(false);
            probeMap.remove(key);
        }
        // common
        common.addAll(current);
        common.removeAll(added);
        common.removeAll(removed);
        for (final String key : common)
        {
            final Probe probe = configMap.get(key);
            final Timestamp lm = probe.getProbeConfig().getLastModifiedOn();
            if (lm.after(lastRefreshed.getTime()))
            {
                final ScheduledFuture<?> future = probeMap.get(key);
                future.cancel(false);
                final ScheduledFuture<?> newFuture = pool.scheduleAtFixedRate(probe,
                        probe.getProbeConfig().getDelayTime(), probe.getProbeConfig().getPollingInterval(),
                        TimeUnit.SECONDS);
                probeMap.replace(key, newFuture);
            }
        }
    }

    public void start()
    {
        LOG.info("Starting: " + running + " : " + engineThread + " alive -> "
                + (null != engineThread ? engineThread.isAlive() : null));
        if (!isRunning())
        {
            if (null == engineThread || ! engineThread.isAlive() )
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
            pool.shutdown();
            pool.awaitTermination(2, TimeUnit.MINUTES);
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
