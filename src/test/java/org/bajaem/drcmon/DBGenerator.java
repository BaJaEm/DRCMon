
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" })
public abstract class DBGenerator
{

    protected static final Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());

    protected static final String user = "testUser";

    private static final Logger LOG = LogManager.getLogger(DBGenerator.class);

    @Autowired
    DataSource dataSource;

    @LocalServerPort
    protected int port;

    @Before
    public void createDB() throws SQLException, FileNotFoundException
    {
        try (final Connection conn = dataSource.getConnection())
        {
            assertNotNull(conn);
            for (final String s : new String[] { "/sql/key.sql", "/sql/probe_type.sql", "/sql/probe_config.sql",
                    "/sql/probe_response.sql" })
            {
                final URL u = DBGenerator.class.getResource(s);
                RunScript.execute(conn, new FileReader(u.getFile()));
            }
        }
    }

    @After
    public void removeDB() throws SQLException
    {
        try (final Connection conn = dataSource.getConnection())
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
    }
}
