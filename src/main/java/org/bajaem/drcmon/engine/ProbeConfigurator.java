
package org.bajaem.drcmon.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.probe.Probe;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.bajaem.drcmon.respository.ProbeResponseRepository;
import org.bajaem.drcmon.respository.ProbeTypeRepository;
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

    @Autowired
    private final ProbeMarkerCache probeCache;

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
            final ProbeTypeRepository _typeRepo, final ProbeMarkerCache _probeCache)
    {
        LOG.info("constructor probe configer");
        configRepo = _configRepo;
        responseRepo = _responseRepo;
        typeRepo = _typeRepo;
        probeCache = _probeCache;
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
    private static ProbeConfigurator probeConfigurator(final ProbeConfigRepository _configRepo,
            final ProbeResponseRepository _responseRepo, final ProbeTypeRepository _typeRepo,
            final ProbeMarkerCache _probeCache)
    {
        LOG.info("static probe configer");
        if (null == config)
        {
            config = new ProbeConfigurator(_configRepo, _responseRepo, _typeRepo, _probeCache);
        }
        return config;
    }

    Map<String, Probe> getProbes()
    {
        final Map<String, Probe> probes = new HashMap<>();
        final Iterable<ProbeConfig> configs = configRepo.findAll();
        for (final ProbeConfig config : configs)
        {
            if (config.isEnabled())
            {
                final Class<? extends Probe> p = probeCache.getConfig2Probe().get(config.getClass());
                try
                {
                    final Constructor<? extends Probe> cons = p.getConstructor(config.getClass());
                    final Probe probe = cons.newInstance(config);
                    probes.put(probe.getUniqueKey(), probe);
                }
                catch (final NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e)
                {
                    LOG.fatal("Could not create instance of Probe - please check configuration" + config.toString(), e);
                }
            }
        }

        return probes;
    }
}
