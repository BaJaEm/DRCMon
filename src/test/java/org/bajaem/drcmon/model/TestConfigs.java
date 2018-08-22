package org.bajaem.drcmon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
public class TestConfigs extends DBGenerator
{
    private static final Logger LOG = LogManager.getLogger();

    private static final Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());

    private static final String user = "testUser";

    @Autowired
    private ProbeConfigRepository configRepo;

    @Test
    public void testExists()
    {
        assertNotNull(configRepo);
    }

    private void configDefaults(final ProbeConfig conf)
    {
        conf.setArtifactId(null);
        conf.setCreatedBy(user);
        conf.setCreatedOn(now);
        conf.setDelayTime(0);
        conf.setLastModifiedBy(user);
        conf.setLastModifiedOn(now);
        conf.setPollingInterval(30);
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
        final PingProbeConfig conf = new PingProbeConfig();
        configDefaults(conf);
        conf.setHost("127.0.0.1");
        configRepo.save(conf);
        checkUpdateDelete(conf.getId());

    }

    @Test
    public void testPortMonProbeConfig()
    {
        final PortMonProbeConfig conf = new PortMonProbeConfig();
        configDefaults(conf);
        conf.setHost("127.0.0.1");
        conf.setPort(8080);
        configRepo.save(conf);

        checkUpdateDelete(conf.getId());

    }

    @Test
    public void testSQLProbeConfig() throws UnknownHostException
    {
        final SQLQueryProbeConfig conf = new SQLQueryProbeConfig();
        configDefaults(conf);
        conf.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        conf.setKeyFile("keyFile");
        conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQueryProbe'");
        conf.setExpected("SQLQueryProbe");
        configRepo.save(conf);

        LOG.info(conf.getId());

        final SQLQueryProbeConfig conf2 = (SQLQueryProbeConfig) configRepo.findById(conf.getId()).get();
        final String url = conf2.getUrl();
        assertNotNull(url);
        assertEquals("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", url);
        final String expected = conf2.getExpected();
        assertNotNull(expected);
        assertEquals("SQLQueryProbe", expected);
        final String keyFile = conf2.getKeyFile();
        assertNotNull(keyFile);
        assertEquals("keyFile", keyFile);

        checkUpdateDelete(conf.getId());
    }

    @Test
    public void testRESTGetProbeConfig() throws UnknownHostException
    {
        final RESTGetProbeConfig conf = new RESTGetProbeConfig();
        configDefaults(conf);
        conf.setUrl("http://localhost:8080/api/portTypes/Ping");
        conf.setPath("description");
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
        configRepo.save(conf);

        LOG.info(conf.getId());

        final RESTGetProbeConfig conf2 = (RESTGetProbeConfig) configRepo.findById(conf.getId()).get();
        final String url = conf2.getUrl();
        assertNotNull(url);
        assertEquals("http://localhost:8080/api/portTypes/Ping", url);
        final String expected = conf2.getExpected();
        assertNotNull(expected);
        assertEquals("Ping using java isReachable method - may or maynot be ICMP", expected);
        final String keyFile = conf2.getKeyFile();
        assertNull(keyFile);
        final String path = conf2.getPath();
        assertNotNull(path);
        assertEquals("description", path);

        checkUpdateDelete(conf.getId());
    }
}
