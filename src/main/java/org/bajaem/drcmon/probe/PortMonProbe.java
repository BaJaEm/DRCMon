
package org.bajaem.drcmon.probe;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.model.PortMonProbeConfig;

@ProbeMarker(config = PortMonProbeConfig.class, typeName = "PortMon")
public class PortMonProbe extends Probe
{

    private static final Logger LOG = LogManager.getLogger(PortMonProbe.class);

    private final PortMonProbeConfig myConfig;

    public PortMonProbe(final PortMonProbeConfig _probeConfig)
    {
        super(_probeConfig);
        myConfig = _probeConfig;
    }

    @Override
    public Response probe()
    {
        try
        {
            final Socket s = new Socket(myConfig.getHost(), myConfig.getPort());
            s.close();
            return new Response(true);
        }
        catch (final IOException e)
        {
            LOG.info("Could not connect to: " + myConfig.getHost() + ":" + myConfig.getPort());
            return new Response(false, e.getMessage(), e);
        }
    }

    @Override
    public String getUniqueKey()
    {
        return myConfig.getHost() + ":" + myConfig.getPort();
    }

}
