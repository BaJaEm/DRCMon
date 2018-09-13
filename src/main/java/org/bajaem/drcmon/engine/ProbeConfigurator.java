
package org.bajaem.drcmon.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.model.Configurable;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.mq.MessageSender;
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

    @Autowired
    private final MessageSender sender;

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
            final ProbeTypeRepository _typeRepo, final ProbeMarkerCache _probeCache, final MessageSender _sender)
    {
        LOG.info("constructor probe configer");
        configRepo = _configRepo;
        responseRepo = _responseRepo;
        typeRepo = _typeRepo;
        probeCache = _probeCache;
        sender = _sender;
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
            final ProbeMarkerCache _probeCache, final MessageSender _sender)
    {
        LOG.info("static probe configer");
        if (null == config)
        {
            config = new ProbeConfigurator(_configRepo, _responseRepo, _typeRepo, _probeCache, _sender);
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
                final Class<? extends Probe> p = probeCache.getType2Probe().get(config.getProbeType().getName());
                final Class<? extends Configurable> c = probeCache.getType2Config()
                        .get(config.getProbeType().getName());
                try
                {
                    // Reflection voodoo here --->
                    final Constructor<? extends Configurable> conCons = c.getConstructor(config.getClass(),
                            probeCache.getClass(), MessageSender.class);
                    LOG.info(c.getClass());
                    final Constructor<? extends Probe> probeCons = p.getConstructor(c);
                    final Configurable configurable = conCons.newInstance(config, probeCache, sender);
                    final Probe probe = probeCons.newInstance(configurable);
                    // Reflection voodoo ends
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
