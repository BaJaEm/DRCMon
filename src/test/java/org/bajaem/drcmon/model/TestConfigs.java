package org.bajaem.drcmon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.probe.PingProbeConfig;
import org.bajaem.drcmon.probe.PortMonProbeConfig;
import org.bajaem.drcmon.probe.RESTGetProbeConfig;
import org.bajaem.drcmon.probe.SQLQueryProbeConfig;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
public class TestConfigs extends DBGenerator
{
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private ProbeConfigRepository configRepo;

    @Test
    public void testExists()
    {
        assertNotNull(configRepo);
    }

    public void checkUpdateDelete(final long id)
    {
        final ProbeConfig conf2 = configRepo.findById(id).get();
        assertNotNull(conf2);
        conf2.setArtifactId("newID");
        configRepo.save(conf2);
        final ProbeConfig conf3 = configRepo.findById(id).get();
        assertNotNull(conf3);
        assertEquals("newID", conf3.getArtifactId());
        configRepo.delete(conf2);
        try
        {
            configRepo.findById(id).get();
            fail("Element should not exist");
        }
        catch (final NoSuchElementException e)
        {
            // success!!!
        }

    }

    @Test
    public void testPingProbeConfig()
    {
        final PingProbeConfig conf = newPingProbeConfig();
        configRepo.save(conf.getConfig());
        checkUpdateDelete(conf.getConfig().getId());
    }

    @Test
    public void testPortMonProbeConfig()
    {
        final PortMonProbeConfig conf = newPortMonProbeConfig("127.0.0.1", 8080);
        configRepo.save(conf.getConfig());

        checkUpdateDelete(conf.getConfig().getId());

    }

    @Test
    public void testSQLProbeConfig() throws UnknownHostException
    {
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig();
        configRepo.save(conf.getConfig());

        final SQLQueryProbeConfig conf2 = new SQLQueryProbeConfig(configRepo.findById(conf.getConfig().getId()).get(),
                cache, sender);
        final String url = conf2.getUrl();
        assertNotNull(url);
        assertEquals("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", url);
        final String expected = conf2.getExpected();
        assertNotNull(expected);
        assertEquals("SQLQueryProbe", expected);
        final ProbeKey key = conf.getConfig().getProbeKey();
        assertNotNull(key);

        checkUpdateDelete(conf.getConfig().getId());
    }

    @Test
    public void testRESTGetProbeConfig() throws UnknownHostException
    {
        final RESTGetProbeConfig conf = newRESTGetProbeConfig();
        configRepo.save(conf.getConfig());

        LOG.info(conf.getConfig().getId());

        final RESTGetProbeConfig conf2 = new RESTGetProbeConfig(configRepo.findById(conf.getConfig().getId()).get(),
                cache, sender);
        LOG.info("CREATED ON: " + conf2.getConfig().getCreatedOn());
        final String url = conf2.getUrl();
        assertNotNull(url);
        assertEquals(conf.getUrl(), url);
        final String expected = conf2.getExpected();
        assertNotNull(expected);
        assertEquals("Ping using java isReachable method - may or maynot be ICMP", expected);
        final ProbeKey key = conf.getConfig().getProbeKey();
        assertNotNull(key);
        final String path = conf2.getPath();
        assertNotNull(path);
        assertEquals("description", path);

        checkUpdateDelete(conf.getConfig().getId());
    }
}
