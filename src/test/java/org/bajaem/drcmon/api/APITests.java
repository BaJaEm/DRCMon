
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
import com.fasterxml.jackson.databind.node.ObjectNode;

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

        final ObjectNode node = (ObjectNode) new ObjectMapper().convertValue(conf.getConfig(), JsonNode.class);
        final String url = baseURL + "api/probeConfigs";
        node.put("probeType", getURLforProbeType("Ping"));
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
        final ObjectNode node = (ObjectNode) new ObjectMapper().convertValue(conf.getConfig(), JsonNode.class);
        node.put("probeType", getURLforProbeType("PortMon"));
        LOG.info("ProbeType URL: " + node.get("probeType"));
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

        final ObjectNode node = (ObjectNode) new ObjectMapper().convertValue(conf.getConfig(), JsonNode.class);
        node.put("probeType", getURLforProbeType("RESTGet"));
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

        final ObjectNode node = (ObjectNode) new ObjectMapper().convertValue(conf.getConfig(), JsonNode.class);
        node.put("probeType", getURLforProbeType("SQLQuery"));
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
