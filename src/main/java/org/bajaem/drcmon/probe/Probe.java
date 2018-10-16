
package org.bajaem.drcmon.probe;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.SystemUserWrapper;
import org.bajaem.drcmon.engine.ProbeConfigurator;
import org.bajaem.drcmon.model.Configurable;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.ProbeResponse;
import org.bajaem.drcmon.mq.MessageSender;
import org.bajaem.drcmon.respository.ProbeResponseRepository;
import org.springframework.stereotype.Component;

/**
 *
 * Abstract base class for Network Probes
 *
 */
@Component
public abstract class Probe implements Runnable
{

    private static final Logger LOG = LogManager.getLogger(Probe.class);

    protected final ProbeConfig probeConfig;

    protected final Configurable config;

    protected static InetAddress localhost;
    static
    {
        try
        {
            localhost = InetAddress.getLocalHost();
        }
        catch (final UnknownHostException e)
        {
            LOG.fatal("Could not aquire localhost", e);
            throw new RuntimeException(e);
        }
    }

    public Probe(final Configurable _config)
    {
        config = _config;
        probeConfig = config.getConfig();

    }

    /**
     * Execute the probe logic and return a {@link Response} Object with the
     * return value for the probe action.
     */
    public abstract Response probe();

    /**
     * probe method to define a key that identifies this probe instance. This
     * value is used as the hash key for the Probe;
     */
    public abstract String getUniqueKey();

    @Override
    public void run()
    {
        final Instant start = Instant.now();
        final Response r = probe();
        final Instant end = Instant.now();
        final ThreadLocal<ProbeResponse> resp = new ThreadLocal<>();
        SystemUserWrapper.executeAsSystem(() -> resp.set(storeResponse(r, localhost, start, end)));

        if (null != config.getSender() && config.getConfig().getProbeCategory() != null)
        {
            config.getSender().sendMessage(resp.get(), config.getConfig().getProbeCategory().getChannel());
        }
    }

    private ProbeResponse storeResponse(final Response r, final InetAddress addr, final Instant start,
            final Instant end)
    {
        try
        {
            final ProbeConfigurator configer = ProbeConfigurator.getProbeConfigurator();
            LOG.info("Duration: " + (end.toEpochMilli() - start.toEpochMilli()) + " " + toString() + " for " + r);
            final ProbeResponseRepository repo = configer.getProbeResponseRepository();
            final ProbeResponse resp = new ProbeResponse();

            resp.setStartTime(new Timestamp(start.toEpochMilli()));
            resp.setEndTime(new Timestamp(end.toEpochMilli()));
            if (r.getError() != null)
            {
                try (final StringWriter sw = new StringWriter())
                {
                    try (final PrintWriter pw = new PrintWriter(sw))
                    {
                        r.getError().printStackTrace(pw);
                        resp.setError(sw.toString());
                    }
                }
                catch (final IOException e)
                {
                    LOG.error(e.getMessage(), e);
                }
            }

            resp.setErrorMessage(r.getErrorMessage());
            resp.setProbeConfig(probeConfig);

            resp.setSuccess(r.isSuccess());
            repo.save(resp);
            return resp;
        }
        catch (final Throwable e)
        {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    public ProbeConfig getProbeConfig()
    {
        return probeConfig;
    }

    @Override
    public String toString()
    {
        return this.getClass() + " @" + probeConfig.getPollingInterval() + " after: " + probeConfig.getDelayTime();
    }
}
