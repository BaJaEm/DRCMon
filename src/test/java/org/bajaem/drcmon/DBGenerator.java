
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBGenerator
{

    private static final Logger LOG = LogManager.getLogger(DBGenerator.class);

    @Autowired
    DataSource dataSource;

    public void createDB() throws SQLException, FileNotFoundException
    {
        final Connection conn = dataSource.getConnection();
        try
        {
            conn.createStatement().execute("DROP SEQUENCE key_seq");
            conn.createStatement().execute("DROP TABLE probe_response");
            conn.createStatement().execute("DROP TABLE probe_config");
            conn.createStatement().execute("DROP TABLE probe_type");
        }
        catch (final SQLException e)
        {
            LOG.warn(e);
        }

        assertNotNull(conn);
        for (final String s : new String[] { "/sql/key.sql", "/sql/probe_type.sql", "/sql/probe_config.sql",
                "/sql/probe_response.sql" })
        {
            final URL u = DBGenerator.class.getResource(s);
            RunScript.execute(conn, new FileReader(u.getFile()));
        }

    }

    @Test
    public void checkDataSource() throws FileNotFoundException, SQLException
    {
        assertNotNull(dataSource);

        createDB();
    }

}
