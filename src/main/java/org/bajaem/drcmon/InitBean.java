package org.bajaem.drcmon;

import java.net.UnknownHostException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.engine.MonitorEngine;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.bajaem.drcmon.model.PortMonProbeConfig;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.RESTGetProbeConfig;
import org.bajaem.drcmon.model.SQLQueryProbeConfig;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.bajaem.drcmon.util.SystemClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitBean // implements CommandLineRunner
{
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private MonitorEngine eng;

    // @Override
    public void run(final String... args) throws UnknownHostException
    {
        LOG.info("Initialize engine");
        eng.start();
    }

    @Autowired
    private ProbeConfigRepository configRepo;

    @Autowired
    private ProbeMarkerCache cache;

    private final Timestamp now = SystemClock.currentTime();

    private final String user = "testUser";

    private ProbeConfig configDefaults()
    {
        final ProbeConfig conf = new ProbeConfig();
        conf.setArtifactId(null);
        conf.setCreatedBy(user);
        conf.setCreatedOn(now);
        conf.setDelayTime(0);
        conf.setLastModifiedBy(user);
        conf.setLastModifiedOn(now);
        conf.setPollingInterval(30);
        return conf;
    }

    public void createProbes()
    {
        {
            final PingProbeConfig conf = new PingProbeConfig(configDefaults(), cache);
            ;
            conf.setHost("127.0.0.1");
            configRepo.save(conf.getConfig());
        }
        {
            final PortMonProbeConfig conf = new PortMonProbeConfig(configDefaults(), cache);
            configDefaults();
            conf.setHost("127.0.0.1");
            conf.setPort(8080);
            configRepo.save(conf.getConfig());
        }
        {
            final SQLQueryProbeConfig conf = new SQLQueryProbeConfig(configDefaults(), cache);
            configDefaults();
            conf.setUrl("jdbc:h2:./foo;AUTO_SERVER=true");
            conf.setQuery("SELECT name FROM probe_type WHERE name = 'SQLQuery'");
            conf.setExpected("SQLQuery");
            configRepo.save(conf.getConfig());
        }
        {
            final RESTGetProbeConfig conf = new RESTGetProbeConfig(configDefaults(), cache);
            configDefaults();
            conf.setUrl("http://localhost:8080/api/probeTypes/Ping");
            conf.setPath("description");
            conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
            configRepo.save(conf.getConfig());
        }
    }
}