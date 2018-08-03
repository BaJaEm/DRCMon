package org.bajaem.netmon.drcmon.probe;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.configuration.ProbeMarker;
import org.bajaem.netmon.drcmon.model.ProbeConfig;

@ProbeMarker(name = "Ping")
public class PingProbe extends Probe
{
    private static final Logger LOG = LogManager.getLogger(PingProbe.class);

    private static final int TIMEOUT = 30;

    public PingProbe(final ProbeConfig _probeConfig)
    {
        super(_probeConfig);
        LOG.trace("New Probe... " + getProbeConfig().getHost());
    }

    @Override
    public Response probe()
    {
        try
        {
            return new Response(getProbeConfig().getHost().isReachable(TIMEOUT));
        }
        catch (final IOException e)
        {
            return new Response(false, e.getMessage(), e);
        }

    }

    public String toString()
    {
        return getProbeConfig().getHost() + " -- " + super.toString();
    }

}
