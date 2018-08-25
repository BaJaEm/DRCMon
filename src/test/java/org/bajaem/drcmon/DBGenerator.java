
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.bajaem.drcmon.model.PortMonProbeConfig;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.RESTGetProbeConfig;
import org.bajaem.drcmon.model.SQLQueryProbeConfig;
import org.bajaem.drcmon.util.Key;
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

    private static final Logger LOG = LogManager.getLogger();

    protected File goodWebKeyFile;

    protected File badWebKeyFile;

    protected File goodSQLKeyFile;

    protected File badSQLKeyFile;

    protected String baseURL;

    protected Key goodWebKey;

    protected Key goodSQLKey;

    protected String webUser;

    protected String webPassword;

    @Autowired
    DataSource dataSource;

    @LocalServerPort
    protected int port;

    @Before
    public void createDB() throws SQLException, IOException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
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

        goodWebKeyFile = File.createTempFile("goodTempKey", null);
        Key.encryptKey(goodWebKeyFile.getAbsolutePath(), "b", "b");
        goodWebKey = Key.decryptKey(goodWebKeyFile.getAbsolutePath());

        badWebKeyFile = File.createTempFile("badTempKey", null);
        Key.encryptKey(badWebKeyFile.getAbsolutePath(), "bad", "bad");

        goodSQLKeyFile = File.createTempFile("goodTempKey", null);
        Key.encryptKey(goodSQLKeyFile.getAbsolutePath(), "System", "");
        goodSQLKey = Key.decryptKey(goodSQLKeyFile.getAbsolutePath());

        badSQLKeyFile = File.createTempFile("badTempKey", null);
        Key.encryptKey(badSQLKeyFile.getAbsolutePath(), "bad", "bad");

        webUser = goodWebKey.getId();
        webPassword = goodWebKey.getSecret();

        baseURL = "http://localhost:" + port + "/";
    }

    protected void initializConfig(final ProbeConfig config)
    {
        config.setArtifactId(null);
        config.setCreatedBy(user);
        config.setCreatedOn(now);
        config.setDelayTime(0);
        config.setLastModifiedBy(user);
        config.setLastModifiedOn(now);
        config.setPollingInterval(30);
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

    protected PingProbeConfig newPingProbeConfig(final String host)
    {
        final PingProbeConfig conf = new PingProbeConfig();
        initializConfig(conf);
        conf.setHost(host);
        return conf;
    }

    protected PingProbeConfig newPingProbeConfig()
    {
        return (newPingProbeConfig("127.0.0.1"));
    }

    protected PortMonProbeConfig newPortMonProbeConfig()
    {
        return newPortMonProbeConfig("127.0.0.1", port);
    }

    protected PortMonProbeConfig newPortMonProbeConfig(final String host, final int _port)
    {
        final PortMonProbeConfig conf = new PortMonProbeConfig();
        initializConfig(conf);
        conf.setHost(host);
        conf.setPort(_port);
        return conf;
    }

    protected SQLQueryProbeConfig newSQLQueryProbeConfig()
    {
        return newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                "keyFile", //
                "SELECT name FROM probe_type WHERE name = 'SQLQueryProbe'", //
                "SQLQueryProbe"//
        );
    }

    protected SQLQueryProbeConfig newSQLQueryProbeConfig(final String _url, final String _keyFile, final String _query,
            final String _expected)
    {
        final SQLQueryProbeConfig conf = new SQLQueryProbeConfig();
        initializConfig(conf);
        conf.setUrl(_url);
        conf.setKeyFile(_keyFile);
        conf.setQuery(_query);
        conf.setExpected(_expected);
        return conf;
    }

    protected RESTGetProbeConfig newRESTGetProbeConfig()
    {
        return newRESTGetProbeConfig(//
                "http://localhost:" + port + "/api/portTypes/Ping", //
                "description", //
                "Ping using java isReachable method - may or maynot be ICMP", //
                goodWebKeyFile.getAbsolutePath()//
        );
    }

    protected RESTGetProbeConfig newRESTGetProbeConfig(final String _url, final String _path, final String _expected,
            final String _keyFile)
    {
        final RESTGetProbeConfig conf = new RESTGetProbeConfig();
        initializConfig(conf);
        conf.setUrl(_url);
        conf.setPath(_path);
        conf.setExpected(_expected);
        conf.setKeyFile(_keyFile);
        return conf;
    }
}
