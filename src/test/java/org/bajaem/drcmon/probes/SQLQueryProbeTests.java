
package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.model.SQLQueryProbeConfig;
import org.bajaem.drcmon.probe.Probe;
import org.bajaem.drcmon.probe.Response;
import org.bajaem.drcmon.probe.SQLQueryProbe;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class SQLQueryProbeTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    private final SQLQueryProbeConfig conf = new SQLQueryProbeConfig();

    @Before
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void init()
    {
        LOG.trace("Starting test");

        initializConfig(conf);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testGood()
    {
        conf.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        conf.setKeyFile(goodSQLKeyFile.getAbsolutePath());
        conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQuery'");
        conf.setExpected("SQLQuery");
        final Probe p = new SQLQueryProbe(conf);
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
    public void testNoUser()
    {
        conf.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQuery'");
        conf.setExpected("SQLQuery");
        final Probe p = new SQLQueryProbe(conf);
        final Response response = p.probe();
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
        conf.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQuery'");
        conf.setExpected("SQLQuery");
        conf.setKeyFile("BadFile");
        try
        {
            new SQLQueryProbe(conf);
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
        conf.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQuery'");
        conf.setExpected("SQLQuery");
        final Probe p = new SQLQueryProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadQuery()
    {
        conf.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        conf.setQuery("FOO");
        conf.setExpected("FOO");
        conf.setKeyFile(goodSQLKeyFile.getAbsolutePath());
        final Probe p = new SQLQueryProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testWrongResponse()
    {
        conf.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQuery'");
        conf.setExpected("FOO");
        conf.setKeyFile(goodSQLKeyFile.getAbsolutePath());
        final Probe p = new SQLQueryProbe(conf);
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
    public void testBadURL()
    {
        conf.setUrl("foo");
        conf.setKeyFile(goodSQLKeyFile.getAbsolutePath());
        conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQuery'");
        conf.setExpected("SQLQuery");
        final Probe p = new SQLQueryProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

}
