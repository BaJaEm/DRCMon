
package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.model.RESTGetProbeConfig;
import org.bajaem.drcmon.probe.RESTGetProbe;
import org.bajaem.drcmon.probe.Response;
import org.bajaem.drcmon.util.Key;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class RESTGetProbeTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    private final RESTGetProbeConfig conf = new RESTGetProbeConfig();

    private File goodKeyFile;
    
    private File badKeyFile;

    @Before
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void init() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException
    {
        LOG.trace("Starting test");

        conf.setArtifactId(null);
        conf.setCreatedBy(user);
        conf.setCreatedOn(now);
        conf.setDelayTime(0);
        conf.setLastModifiedBy(user);
        conf.setLastModifiedOn(now);
        conf.setPollingInterval(30);

        goodKeyFile = File.createTempFile("goodTempKey", null);
        Key.encryptKey(goodKeyFile.getAbsolutePath(), "b", "b");
        
        badKeyFile = File.createTempFile("badTempKey", null);
        Key.encryptKey(badKeyFile.getAbsolutePath(), "bad", "bad");
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testGood()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile(goodKeyFile.getAbsolutePath());
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadResult()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile(goodKeyFile.getAbsolutePath());
        conf.setExpected("Not Good");
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testNoUser()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        LOG.info(response.getErrorMessage());
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadHost()
    {
        conf.setUrl("http://foo.bar:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        LOG.info(response.getErrorMessage());
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadKeyFile()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile("BadFile");
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
        try
        {
            new RESTGetProbe(conf);
            fail("Probe Intialization should fail");
        }
        catch (DRCStartupException e)
        {
            // Correct path
        }
    }
    
    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadUserPassword()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile(badKeyFile.getAbsolutePath());
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        LOG.info(response.getErrorMessage());
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }
}
