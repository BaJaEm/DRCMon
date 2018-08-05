package org.bajaem.netmon.drcmon.probe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.configuration.ProbeMarker;
import org.bajaem.netmon.drcmon.model.ProbeConfig;

/**
 * SQL Query Probe
 * 
 * Probe to query a jdbc compliant database.
 * <p>
 * <list> Database must:
 * <li>have a user with database authentication
 * <li>be a supported database ( H2, postgresql, MS SQL ) The custom </list>
 * <p>
 * <list> configuration contract is:
 * <li>url = <jdbc url to connect to database>
 * <li>query = <SQL Query returning a single row and single String value>
 * <li>expected = <single String value that is expected>
 * <li>user = <database user>
 * <li>password = <database password> </list>
 * <p>
 */
// TODO: better handling of user/password
// TODO: runtime loading of Driver Resources - possible new table with Driver
// binaries?
@ProbeMarker(name = "SQLQueryProbe")
public class SQLQueryProbe extends Probe
{
    private static final Logger LOG = LogManager.getLogger(SQLQueryProbe.class);

    private final String url;

    private final String query;

    private final String expected;

    private final String user;

    private final String password;

    public SQLQueryProbe(final ProbeConfig _probeConfig)
    {
        super(_probeConfig);
        LOG.debug("new Query Probe");
        final Map<String, String> custom = _probeConfig.getCustomConfiguration();
        url = custom.get("url");
        user = custom.get("user");
        password = custom.get("password");
        query = custom.get("query");
        expected = custom.get("expected");

    }

    @Override
    public Response probe()
    {
        try (final Connection connection = DriverManager.getConnection(url, user, password))
        {
            try (final Statement stmt = connection.createStatement())
            {
                try (final ResultSet rs = stmt.executeQuery(query))
                {
                    if (rs.next())
                    {
                        final String value = rs.getString(1);
                        if (null != value && expected.equals(value))
                        {
                            return (new Response(true));
                        }
                    }
                }
            }
        }
        catch (final SQLException e)
        {
            return new Response(false, e.getMessage(), e);
        }
        return new Response(false);
    }

    @Override
    public String getUniqueKey()
    {
        return url + "/" + query;
    }

}
