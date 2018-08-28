
package org.bajaem.drcmon.probe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.exceptions.DRCProbeException;
import org.bajaem.drcmon.model.RESTGetProbeConfig;
import org.bajaem.drcmon.util.DRCBasicAuthRestWeb;
import org.bajaem.drcmon.util.DRCRestTemplate;
import org.bajaem.drcmon.util.DRCWebClient;
import org.bajaem.drcmon.util.JsonTools;
import org.bajaem.drcmon.util.Key;

import com.fasterxml.jackson.databind.JsonNode;

@ProbeMarker(config = RESTGetProbeConfig.class, typeName = "RESTGet")
public class RESTGetProbe extends Probe
{

    private static final Logger LOG = LogManager.getLogger(RESTGetProbe.class);

    private final RESTGetProbeConfig myConfig;

    private final DRCWebClient client;

    private final Key key;

    public RESTGetProbe(final RESTGetProbeConfig _probeConfig)
    {
        super(_probeConfig.getConfig());
        myConfig = _probeConfig;
        LOG.trace("New Probe... " + _probeConfig);

        if (null != myConfig.getKeyFile())
        {
            key = Key.decryptKey(myConfig.getKeyFile());
            client = new DRCBasicAuthRestWeb(myConfig.getUrl(), key.getId(), key.getSecret());
        }
        else
        {
            key = null;
            client = new DRCRestTemplate(myConfig.getUrl());
        }

        // TODO: use factory for new Web client

    }

    @Override
    public Response probe()
    {
        try
        {
            final JsonNode node = client.get();
            return new Response(myConfig.getExpected().equals(JsonTools.getValue(node, myConfig.getPath())));
        }
        catch (final DRCProbeException e)
        {
            return new Response(false, e.getMessage(), e);
        }

    }

    @Override
    public String getUniqueKey()
    {
        return myConfig.getUrl();
    }

}
