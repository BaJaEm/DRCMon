
package org.bajaem.drcmon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TestTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    @Test
    public void checkDataSource() throws FileNotFoundException, SQLException
    {
        assertNotNull(dataSource);
    }

    @Test
    public void connectionTest() throws SQLException
    {
        assertNotNull(dataSource);
        final Connection conn = dataSource.getConnection();
        assertNotNull(conn);
        final DatabaseMetaData md = conn.getMetaData();
        assertNotNull(md);
        final String url = md.getURL();
        assertNotNull(url);
        if (!url.contains("mem"))
        {
            fail("wrong driver: " + url);
        }
        LOG.info(url);
    }

    @Autowired
    JdbcTemplate template;

    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    @Test
    public void testDS()
    {
        assertNotNull(dataSource);
        LOG.info(dataSource.getClass());
        assertNotNull(template);
        LOG.info(template.getClass());
    }
}
