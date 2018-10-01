
package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.probe.Probe;
import org.bajaem.drcmon.probe.Response;
import org.bajaem.drcmon.probe.SQLQueryProbe;
import org.bajaem.drcmon.probe.SQLQueryProbeConfig;
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
                goodSQLKey, //
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
    public void testBadUserPassword()
    {
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                badSQLKey, //
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

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadQuery()
    {
        final SQLQueryProbeConfig conf = newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                goodSQLKey, //
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
                goodSQLKey, //
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
                goodSQLKey, //
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
