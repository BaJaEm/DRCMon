
package org.bajaem.drcmon.probe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.bajaem.drcmon.respository.ProbeResponseRepository;
import org.bajaem.drcmon.respository.ProbeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

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

    private final static Map<String, Class<Probe>> beans = new HashMap<>();

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

    public static Map<String, Class<Probe>> getProbeBeanMap()
    {
        if (beans.isEmpty())
        {
            final ClassPathScanningCandidateComponentProvider scanner;
            scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(ProbeMarker.class));
            for (final BeanDefinition bd : scanner.findCandidateComponents("*"))
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    final Class<Probe> clazz = (Class<Probe>) Class.forName(bd.getBeanClassName());
                    final ProbeMarker m = clazz.getAnnotation(ProbeMarker.class);
                    final String key = m.name() != null ? m.name() : clazz.getName();
                    beans.put(key, clazz);
                }
                catch (final ClassNotFoundException e)
                {
                    LOG.fatal("Could not find class: " + bd.getBeanClassName(), e);
                    throw new RuntimeException(e);
                }
            }
        }
        return beans;
    }

    public Map<String, Probe> getProbes()
    {
        final Map<String, Probe> probes = new HashMap<>();
        final Iterable<ProbeConfig> configs = configRepo.findAll();
        for (final ProbeConfig config : configs)
        {
            final Class<Probe> p = getProbeBeanMap().get(config.getProbeType().getName());
            try
            {
                final Constructor<Probe> cons = p.getConstructor(ProbeConfig.class);
                final Probe probe = cons.newInstance(config);
                probes.put(probe.getUniqueKey(), probe);
            }
            catch (final NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e)
            {
                LOG.fatal("Could not create instance of Probe - please check configuration" + config.toString(), e);
            }
        }

        return probes;
    }
}
