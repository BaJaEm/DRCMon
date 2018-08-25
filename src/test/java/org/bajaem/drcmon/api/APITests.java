
package org.bajaem.drcmon.api;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.exceptions.DRCProbeException;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.bajaem.drcmon.model.PortMonProbeConfig;
import org.bajaem.drcmon.model.RESTGetProbeConfig;
import org.bajaem.drcmon.model.SQLQueryProbeConfig;
import org.bajaem.drcmon.util.DRCBasicAuthRestWeb;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APITests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    private final Map<String, String> headers = new HashMap<String, String>();

    public APITests()
    {
        headers.put("Content-Type", "application/json");
    }

    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    @Test
    public void testCreatePingConfigMappedObject()
    {
        final PingProbeConfig conf = newPingProbeConfig();

        final JsonNode node = new ObjectMapper().convertValue(conf, JsonNode.class);
        final String url = baseURL + "api/probeConfigs";

        final DRCBasicAuthRestWeb client = new DRCBasicAuthRestWeb(url, headers, webUser, webPassword);
        try
        {
            LOG.info(node.toString());
            client.post(node.toString());
        }
        catch (final DRCProbeException e)
        {
            LOG.fatal(e);
            fail(e.getMessage());
        }
    }

    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    @Test
    public void testCreatePortMonConfigMappedObject()
    {
        final PortMonProbeConfig conf = newPortMonProbeConfig();
        final JsonNode node = new ObjectMapper().convertValue(conf, JsonNode.class);
        final String url = baseURL + "api/probeConfigs";

        final DRCBasicAuthRestWeb client = new DRCBasicAuthRestWeb(url, headers, webUser, webPassword);
        try
        {
            LOG.info(node.toString());
            client.post(node.toString());
        }
        catch (final DRCProbeException e)
        {
            LOG.fatal(e);
            fail(e.getMessage());
        }
    }

    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    @Test
    public void testCreateRESTGetConfigMappedObject()
    {
        final RESTGetProbeConfig conf = newRESTGetProbeConfig();

        final JsonNode node = new ObjectMapper().convertValue(conf, JsonNode.class);
        final String url = baseURL + "api/probeConfigs";
        LOG.info(url);
        final String user = goodWebKey.getId();
        final String passwd = goodWebKey.getSecret();
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        final DRCBasicAuthRestWeb client = new DRCBasicAuthRestWeb(url, headers, user, passwd);
        try
        {
            LOG.info(node.toString());
            client.post(node.toString());
        }
        catch (final DRCProbeException e)
        {
            LOG.fatal(e);
            fail(e.getMessage());
        }
    }
    
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    @Test
    public void testCreateSQLQueryProbeConfigMappedObject()
    {
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig();

        final JsonNode node = new ObjectMapper().convertValue(conf, JsonNode.class);
        final String url = baseURL + "api/probeConfigs";
        LOG.info(url);
        final String user = goodWebKey.getId();
        final String passwd = goodWebKey.getSecret();
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        final DRCBasicAuthRestWeb client = new DRCBasicAuthRestWeb(url, headers, user, passwd);
        try
        {
            LOG.info(node.toString());
            client.post(node.toString());
        }
        catch (final DRCProbeException e)
        {
            LOG.fatal(e);
            fail(e.getMessage());
        }
    }

}
