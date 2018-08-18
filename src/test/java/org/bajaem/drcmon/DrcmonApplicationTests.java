
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.bajaem.drcmon.respository.ProbeTypeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class DrcmonApplicationTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private ProbeTypeRepository ptRepo;

    @Autowired
    private ProbeConfigRepository pcRepo;

    public void contextLoads()
    {
        assertNotNull(ptRepo);
        assertNotNull(pcRepo);

    }

    @Test
    public void annotataionTest()
    {
        final ClassPathScanningCandidateComponentProvider scanner;
        scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ProbeMarker.class));
        for (final BeanDefinition bd : scanner.findCandidateComponents("org.bajaem"))
        {
            try
            {
                final Class<?> clazz = Class.forName(bd.getBeanClassName());
                final ProbeMarker m = clazz.getAnnotation(ProbeMarker.class);

                LOG.error(m.config() + m.toString());
            }
            catch (final ClassNotFoundException e)
            {
                LOG.fatal("Could not find class: " + bd.getBeanClassName(), e);
                throw new RuntimeException(e);
            }
        }

        LOG.error("Got Here");
    }

}
