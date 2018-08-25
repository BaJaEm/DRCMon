
package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.model.SQLQueryProbeConfig;
import org.bajaem.drcmon.probe.Probe;
import org.bajaem.drcmon.probe.Response;
import org.bajaem.drcmon.probe.SQLQueryProbe;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class SQLQueryProbeTests extends DBGenerator
{

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testGood()
    {
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                goodSQLKeyFile.getAbsolutePath(), //
                "SELECT name FROM probe_type WHERE name = 'SQLQuery'", //
                "SQLQuery");
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
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig( //
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                null, //
                "SELECT name FROM probe_type WHERE name = 'SQLQuery'", //
                "SQLQuery");
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
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                "BadFile", //
                "SELECT name FROM probe_type WHERE name = 'SQLQuery'", //
                "SQLQuery"); //
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
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                badSQLKeyFile.getAbsolutePath(), "SELECT name FROM probe_type WHERE name = 'SQLQuery'", //
                "SQLQuery");//
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
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                goodSQLKeyFile.getAbsolutePath(), //
                "FOO", //
                "FOO");//
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
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                goodSQLKeyFile.getAbsolutePath(), //
                "SELECT name FROM probe_type WHERE name = 'SQLQuery'", //
                "FOO");//

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
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "foo", //
                goodSQLKeyFile.getAbsolutePath(), //
                "SELECT name FROM probe_type WHERE name = 'SQLQuery'", //
                "SQLQuery");//
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
