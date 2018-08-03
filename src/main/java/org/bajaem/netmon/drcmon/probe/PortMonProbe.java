package org.bajaem.netmon.drcmon.probe;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.configuration.ProbeMarker;
import org.bajaem.netmon.drcmon.model.ProbeConfig;

@ProbeMarker(name = "PortMon")
public class PortMonProbe extends Probe
{
    private static final Logger LOG = LogManager.getLogger(PortMonProbe.class);

    private final Integer port;

    public PortMonProbe(final ProbeConfig _probeConfig)
    {
        super(_probeConfig);
        final String sport = _probeConfig.getCustomConfiguration().get("port");
        port = Integer.parseInt(sport);
    }

    @Override
    public Response probe()
    {
        final ProbeConfig config = getProbeConfig();

        try
        {
            final Socket s = new Socket(config.getHost(), port);
            s.close();
            return new Response(true);
        }
        catch (final IOException e)
        {
            LOG.info("Could not connect to: " + config.getHost() + ":" + port);
            return new Response(false, e.getMessage(), e);
        }

    }

    @Override
    public String getUniqueKey()
    {
        return getProbeConfig().getHost().getHostAddress() + ":" + port;
    }

}
