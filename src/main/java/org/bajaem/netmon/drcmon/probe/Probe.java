package org.bajaem.netmon.drcmon.probe;

import java.net.InetAddress;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.Main;

/**
 * 
 * Abstract base class for Network Probes
 *
 */
public abstract class Probe implements Runnable
{

    private static final Logger LOG = LogManager.getLogger(Probe.class);

    protected final long pollingInterval;

    protected final long delay;

    protected final InetAddress localhost = Main.localhost;

    public Probe()
    {
        this(60);
    }

    public Probe(final long _pollingInterval)
    {
        this(_pollingInterval, 0);
    }

    public Probe(final long _pollingInterval, final long _delay)
    {
        pollingInterval = _pollingInterval;
        delay = _delay;

    }

    /**
     * probe method must be overridden by implementing class;
     */
    public abstract ProbeResponse probe();

    @Override
    public void run()
    {
        final Calendar start = Calendar.getInstance();
        final ProbeResponse r = probe();
        final Calendar end = Calendar.getInstance();
        storeResponse(r, localhost, start, end);

    }

    private void storeResponse(final ProbeResponse r, final InetAddress addr, final Calendar start, final Calendar end)
    {
        LOG.info(
                "Duration: " + (end.getTimeInMillis() - start.getTimeInMillis()) + " " + this.toString() + " for " + r);
    }

    public long getPollingInterval()
    {
        return pollingInterval;
    }

    public long getDelay()
    {
        return delay;
    }

    public String toString()
    {
        return this.getClass() + " -> " + pollingInterval;
    }
}
