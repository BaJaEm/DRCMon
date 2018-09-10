
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

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
import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.configuration.SystemUserWrapper;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.bajaem.drcmon.model.PortMonProbeConfig;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.ProbeKey;
import org.bajaem.drcmon.model.RESTGetProbeConfig;
import org.bajaem.drcmon.model.SQLQueryProbeConfig;
import org.bajaem.drcmon.respository.ProbeKeyRepository;
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

    protected String baseURL;

    protected ProbeKey goodWebKey;

    protected ProbeKey goodSQLKey;

    protected ProbeKey badWebKey;

    protected ProbeKey badSQLKey;

    protected String webUser;

    protected String webPassword;

    @Autowired
    DataSource dataSource;

    @Autowired
    ProbeKeyRepository keyRepo;

    @LocalServerPort
    protected int port;

    @Autowired
    protected ProbeMarkerCache cache;

    @Before
    public void createDB() throws SQLException, IOException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        try (final Connection conn = dataSource.getConnection())
        {
            assertNotNull(conn);
            for (final String s : new String[] { //
                    "/sql/key.sql", //
                    "/sql/probe_type.sql", //
                    "/sql/probe_key.sql", //
                    "/sql/probe_config.sql", //
                    "/sql/probe_response.sql"//
            })
            {
                final URL u = DBGenerator.class.getResource(s);
                LOG.info(u);
                RunScript.execute(conn, new FileReader(u.getFile()));
            }
        }

        goodWebKey = newProbeKey("goodWebKey", "b", "b");
        keyRepo.save(goodWebKey);

        badWebKey = newProbeKey("badWebKey", "bad", "bad");
        keyRepo.save(badWebKey);

        goodSQLKey = newProbeKey("goodSQLKey", "System", "");
        keyRepo.save(goodSQLKey);

        badSQLKey = newProbeKey("badSQLKey", "bad", "bad");
        keyRepo.save(badSQLKey);

        webUser = goodWebKey.getUserId();
        webPassword = goodWebKey.getSecret();

        baseURL = "http://localhost:" + port + "/";
    }

    protected ProbeConfig initializConfig()
    {
        final ProbeConfig config = new ProbeConfig();
        config.setArtifactId(null);
        config.setCreatedBy(user);
        config.setCreatedOn(now);
        config.setDelayTime(0);
        config.setLastModifiedBy(user);
        config.setLastModifiedOn(now);
        config.setPollingInterval(30);
        return config;
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
            conn.createStatement().execute("DROP TABLE probe_key");
        }
        catch (final SQLException e)
        {
            LOG.warn(e);
        }
    }

    public ProbeKey newProbeKey(final String name, final String userId, final String secret)
    {
        final ProbeKey key = new ProbeKey();
        key.setName(name);
        key.setUserId(userId);
        key.setSecret(secret);
        return key;
    }

    protected PingProbeConfig newPingProbeConfig(final String host)
    {
        final ProbeConfig pConfig = initializConfig();
        final PingProbeConfig conf = new PingProbeConfig(pConfig, cache);
        conf.setHost(host);
        pConfig.setProbeType(cache.getProbeTypeByConfig(conf.getClass()));
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
        final ProbeConfig pConfig = initializConfig();
        final PortMonProbeConfig conf = new PortMonProbeConfig(pConfig, cache);
        pConfig.setProbeType(cache.getProbeTypeByConfig(conf.getClass()));
        conf.setHost(host);
        conf.setPort(_port);
        return conf;
    }

    protected SQLQueryProbeConfig newSQLQueryProbeConfig()
    {
        return newSQLQueryProbeConfig(//
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", //
                goodSQLKey, //
                "SELECT name FROM probe_type WHERE name = 'SQLQueryProbe'", //
                "SQLQueryProbe"//
        );
    }

    protected SQLQueryProbeConfig newSQLQueryProbeConfig(final String _url, final ProbeKey _key, final String _query,
            final String _expected)
    {
        final ProbeConfig pConfig = initializConfig();
        final SQLQueryProbeConfig conf = new SQLQueryProbeConfig(pConfig, cache);
        pConfig.setProbeType(cache.getProbeTypeByConfig(conf.getClass()));
        conf.setUrl(_url);
        pConfig.setProbeKey(_key);
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
                goodWebKey//
        );
    }

    protected RESTGetProbeConfig newRESTGetProbeConfig(final String _url, final String _path, final String _expected,
            final ProbeKey key)
    {
        final ProbeConfig pConfig = initializConfig();
        final RESTGetProbeConfig conf = new RESTGetProbeConfig(pConfig, cache);
        pConfig.setProbeType(cache.getProbeTypeByConfig(conf.getClass()));
        pConfig.setProbeKey(key);
        conf.setUrl(_url);
        conf.setPath(_path);
        conf.setExpected(_expected);

        return conf;
    }

    protected String getURLforProbeType(final String name)
    {
        return baseURL + "/api/probeTypes/" + cache.getProbeTypeByName(name).getId();
    }
}
