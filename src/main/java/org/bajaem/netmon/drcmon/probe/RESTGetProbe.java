package org.bajaem.netmon.drcmon.probe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.configuration.ProbeMarker;
import org.bajaem.netmon.drcmon.model.ProbeConfig;
import org.bajaem.netmon.drcmon.util.DRCRestTemplate;
import org.bajaem.netmon.drcmon.util.DRCWebClient;

@ProbeMarker(name = "RESTGetProbe")
public class RESTGetProbe extends Probe
{
    private static final Logger LOG = LogManager.getLogger(RESTGetProbe.class);

    private final String url;

    private final String response;

    private final DRCWebClient client;

    public RESTGetProbe(final ProbeConfig _probeConfig)
    {
        super(_probeConfig);
        LOG.trace("New Probe... " + getProbeConfig().getHost());
        url = getProbeConfig().getCustomConfiguration().get("url");
        response = getProbeConfig().getCustomConfiguration().get("response");
        // TODO: use factory
        client = new DRCRestTemplate(url);

    }

    @Override
    public Response probe()
    {
        return new Response(response.equals(client.get(String.class)));
    }

    @Override
    public String getUniqueKey()
    {
        return url;
    }

}
