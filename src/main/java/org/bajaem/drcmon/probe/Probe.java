
package org.bajaem.drcmon.probe;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.SystemUserWrapper;
import org.bajaem.drcmon.engine.ProbeConfigurator;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.ProbeResponse;
import org.bajaem.drcmon.respository.ProbeResponseRepository;

/**
 *
 * Abstract base class for Network Probes
 *
 */
public abstract class Probe implements Runnable
{

    private static final Logger LOG = LogManager.getLogger(Probe.class);

    protected final ProbeConfig probeConfig;

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

    public Probe(final ProbeConfig _probeConfig)
    {
        probeConfig = _probeConfig;
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
        final Calendar start = Calendar.getInstance();
        final Response r = probe();
        final Calendar end = Calendar.getInstance();
        new SystemUserWrapper().executeAsSystem(() -> storeResponse(r, localhost, start, end));
    }

    private void storeResponse(final Response r, final InetAddress addr, final Calendar start, final Calendar end)
    {
        final ProbeConfigurator configer = ProbeConfigurator.getProbeConfigurator();
        LOG.info("Duration: " + (end.getTimeInMillis() - start.getTimeInMillis()) + " " + toString() + " for " + r);
        final ProbeResponseRepository repo = configer.getProbeResponseRepository();
        final ProbeResponse resp = new ProbeResponse();

        resp.setStartTime(new Timestamp(start.getTimeInMillis()));
        resp.setEndTime(new Timestamp(end.getTimeInMillis()));
        if (r.getError() != null)
        {
            try (final StringWriter sw = new StringWriter())
            {
                final PrintWriter pw = new PrintWriter(sw);
                r.getError().printStackTrace(pw);
                resp.setError(pw.toString());
                pw.close();
            }
            catch (final IOException e)
            {
                LOG.error(e);
            }
        }

        resp.setErrorMessage(r.getErrorMessage());
        resp.setProbeConfig(probeConfig);

        resp.setSuccess(r.isSuccess());
        repo.save(resp);
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
