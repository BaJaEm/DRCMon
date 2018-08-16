
package org.bajaem.drcmon.probe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.exceptions.DRCProbeException;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.util.DRCBasicAuthRestWeb;
import org.bajaem.drcmon.util.DRCWebClient;
import org.bajaem.drcmon.util.JsonTools;
import org.bajaem.drcmon.util.Key;

import com.fasterxml.jackson.databind.JsonNode;

@ProbeMarker(name = "RESTGetProbe")
public class RESTGetProbe extends Probe
{

    private static final Logger LOG = LogManager.getLogger(RESTGetProbe.class);

    private final String url;

    private final String expected;

    private final String path;

    private final DRCWebClient client;

    private final Key key;

    public RESTGetProbe(final ProbeConfig _probeConfig)
    {
        super(_probeConfig);
        LOG.trace("New Probe... " + getProbeConfig().getHost());
        url = getProbeConfig().getCustomConfiguration().get("url");
        expected = getProbeConfig().getCustomConfiguration().get("expected");
        path = getProbeConfig().getCustomConfiguration().get("path");
        key = Key.decryptKey(getProbeConfig().getCustomConfiguration().get("keyFile"));
        // TODO: use factory
        client = new DRCBasicAuthRestWeb(url, key.getId(), key.getSecret());

    }

    @Override
    public Response probe()
    {
        final JsonNode node = client.get();
        try
        {
            return new Response(expected.equals(JsonTools.getValue(node, path)));
        }
        catch (final DRCProbeException e)
        {
            return new Response(false, e.getMessage(), e);
        }

    }

    @Override
    public String getUniqueKey()
    {
        return url;
    }

}
