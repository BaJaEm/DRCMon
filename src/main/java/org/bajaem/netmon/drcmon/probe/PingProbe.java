package org.bajaem.netmon.drcmon.probe;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.Main;

public class PingProbe extends Probe
{
    private static final Logger LOG = LogManager.getLogger(Main.class);
    private final InetAddress target;

    private final int timeout;

    public PingProbe(final long _pollingInterval, final InetAddress _target, final int _timeout)
    {
        super(_pollingInterval);
        target = _target;
        timeout = _timeout;
        LOG.trace("New Probe... " + target);
    }

    public PingProbe(final InetAddress _target, final int _timeout)
    {
        super();
        target = _target;
        timeout = _timeout;

    }

    public PingProbe(final long _pollingInterval, final InetAddress _target)
    {
        this(_pollingInterval, _target, 30);
    }

    public PingProbe(final InetAddress _target)
    {
        this(_target, 30);
    }

    public InetAddress getTarget()
    {
        return target;
    }

    @Override
    public ProbeResponse probe()
    {
        try
        {
            return new ProbeResponse(target.isReachable(timeout));
        }
        catch (final IOException e)
        {
            return new ProbeResponse(false, e.getMessage(), e);
        }

    }

    public String toString()
    {
        return target + " -- " + super.toString();
    }

}
