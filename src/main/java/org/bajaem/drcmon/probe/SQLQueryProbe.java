
package org.bajaem.drcmon.probe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.SQLQueryProbeConfig;
import org.bajaem.drcmon.util.Key;

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
 * <li>keyFile = <file with obfuscated username/password>
 * <p>
 */

// TODO: runtime loading of Driver Resources - possible new table with Driver
// binaries?
@ProbeMarker(config = SQLQueryProbeConfig.class)
public class SQLQueryProbe extends Probe
{

    private static final Logger LOG = LogManager.getLogger(SQLQueryProbe.class);

    private final Key key;

    private final SQLQueryProbeConfig myConfig;

    public SQLQueryProbe(final ProbeConfig _probeConfig)
    {
        super(_probeConfig);
        myConfig = (SQLQueryProbeConfig) _probeConfig;
        LOG.debug("new Query Probe");

        if (null != myConfig.getKeyFile())
        {
            key = Key.decryptKey(myConfig.getKeyFile());
        }
        else
        {
            key = null;
        }

    }

    @Override
    public Response probe()
    {
        try (final Connection connection = DriverManager.getConnection(myConfig.getUrl(), key.getId(), key.getSecret()))
        {
            try (final Statement stmt = connection.createStatement())
            {
                try (final ResultSet rs = stmt.executeQuery(myConfig.getQuery()))
                {
                    if (rs.next())
                    {
                        final String value = rs.getString(1);
                        if (null != value && myConfig.getExpected().equals(value))
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
        return myConfig.getUrl() + "/" + myConfig.getQuery();
    }

}