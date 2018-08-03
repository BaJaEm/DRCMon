package org.bajaem.netmon.drcmon.probe;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.model.ProbeConfig;

public class PortMonProbe extends Probe
{

    public PortMonProbe(final ProbeConfig _probeConfig)
    {
        super(_probeConfig);
    }

    private static final Logger LOG = LogManager.getLogger(PortMonProbe.class);

    @Override
    public Response probe()
    {
        final ProbeConfig config = getProbeConfig();

        try
        {
            final Socket s = new Socket(config.getHost(), config.getPort());
            s.close();
            return new Response(true);
        }
        catch (final IOException e)
        {
            LOG.info("Could not connect to: " + config.getHost() + ":" + config.getPort());
            return new Response(false, e.getMessage(), e);
        }

    }

}
