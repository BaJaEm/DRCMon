
package org.bajaem.drcmon.probe;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;

@ProbeMarker(config = PingProbeConfig.class, typeName = "Ping")
public class PingProbe extends Probe
{

    static final Logger LOG = LogManager.getLogger(PingProbe.class);

    private static final int TIMEOUT = 10000;

    private final PingProbeConfig myConfig;

    public PingProbe(final PingProbeConfig _probeConfig)
    {
        super(_probeConfig);
        myConfig = _probeConfig;
        LOG.trace("New Probe... " + myConfig.getHost());
    }

    @Override
    public Response probe()
    {
        try
        {
            return new Response(myConfig.getInetAddress().isReachable(TIMEOUT));
        }
        catch (final IOException e)
        {
            return new Response(false, e.getMessage(), e);
        }

    }

    @Override
    public String toString()
    {
        return myConfig.getHost() + " -- " + super.toString();
    }
}
