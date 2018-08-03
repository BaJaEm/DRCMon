package org.bajaem.netmon.drcmon.probe;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.model.ProbeConfig;
import org.bajaem.netmon.drcmon.respository.ProbeConfigRepository;
import org.bajaem.netmon.drcmon.respository.ProbeResponseRepository;
import org.bajaem.netmon.drcmon.respository.ProbeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProbeConfigurator
{
    private static final Logger LOG = LogManager.getLogger(ProbeConfigurator.class);
    private static ProbeConfigurator config;

    // TODO - can these be dynamically bundled?
    @Autowired
    private final ProbeConfigRepository configRepo;

    @Autowired
    private final ProbeResponseRepository responseRepo;

    @Autowired
    private final ProbeTypeRepository typeRepo;

    public ProbeResponseRepository getProbeResponseRepository()
    {
        return responseRepo;
    }

    public ProbeConfigRepository getProbeConfigRepository()
    {
        return configRepo;
    }

    public ProbeTypeRepository getProbeTypeRepository()
    {
        return typeRepo;
    }

    public ProbeConfigurator(final ProbeConfigRepository _configRepo, final ProbeResponseRepository _responseRepo,
            final ProbeTypeRepository _typeRepo)
    {
        LOG.info("constructor probe configer");
        configRepo = _configRepo;
        responseRepo = _responseRepo;
        typeRepo = _typeRepo;
    }

    /**
     * This might be fragile based on the life-cycle of the IOC Container. The
     * Object will not have been initialized until the Container initialization
     * is complete.
     */
    public static ProbeConfigurator getProbeConfigurator()
    {
        return config;
    }

    @Bean
    public static ProbeConfigurator probeConfigurator(final ProbeConfigRepository _configRepo,
            final ProbeResponseRepository _responseRepo, final ProbeTypeRepository _typeRepo)
    {
        LOG.info("static probe configer");
        if (null == config)
        {
            config = new ProbeConfigurator(_configRepo, _responseRepo, _typeRepo);
        }
        return config;
    }

    // TODO : use more elegant way to create new probe instances
    public Map<String, Probe> getProbes()
    {
        final Map<String, Probe> probes = new HashMap<>();
        final Iterable<ProbeConfig> configs = configRepo.findAll();
        for (final ProbeConfig config : configs)
        {
            if (config.getProbeType().getName().equals("Ping"))
            {

                probes.put(config.getHost().toString(), new PingProbe(config));
            }
        }

        return probes;
    }
}
