package org.bajaem.netmon.drcmon.probe;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.model.ProbeConfig;
import org.bajaem.netmon.drcmon.model.ProbeResponse;
import org.bajaem.netmon.drcmon.respository.ProbeResponseRepository;

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
        catch (UnknownHostException e)
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
     * probe method must be overridden by implementing class;
     */
    public abstract Response probe();

    @Override
    public void run()
    {
        final Calendar start = Calendar.getInstance();
        final Response r = probe();
        final Calendar end = Calendar.getInstance();
        storeResponse(r, localhost, start, end);

    }

    private void storeResponse(final Response r, final InetAddress addr, final Calendar start, final Calendar end)
    {
        final ProbeConfigurator configer = ProbeConfigurator.getProbeConfigurator();
        LOG.info(
                "Duration: " + (end.getTimeInMillis() - start.getTimeInMillis()) + " " + this.toString() + " for " + r);
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

    public String toString()
    {
        return this.getClass() + " @" + probeConfig.getPollingInterval() + " after: " + probeConfig.getDelayTime();
    }
}
