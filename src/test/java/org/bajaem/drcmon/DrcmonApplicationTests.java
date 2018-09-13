
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

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
import org.springframework.security.test.context.support.WithMockUser;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

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

    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    @Test
    public void annotataionWithClassGraphTest()
    {
        final Calendar start = Calendar.getInstance();
        try (final ScanResult scanResult = new ClassGraph() //
                .enableAnnotationInfo() //
                .whitelistPackages("*") //
                .scan())
        {
            final ClassInfoList pTypes = scanResult
                    .getClassesWithAnnotation("org.bajaem.drcmon.configuration.ProbeMarker");

            for (final ClassInfo ci : pTypes)
            {
                final String className = ci.getName();
                try
                {
                    final Class<?> clazz = Class.forName(className);
                    final ProbeMarker m = clazz.getAnnotation(ProbeMarker.class);

                    LOG.error(m.config() + m.toString());
                }
                catch (final ClassNotFoundException e)
                {
                    LOG.fatal("Could not find class: " + className, e);
                    throw new RuntimeException(e);
                }
            }
        }
        final Calendar end = Calendar.getInstance();
        LOG.error("annotataionWithClassGraphTest: " + (end.getTimeInMillis() - start.getTimeInMillis()));
    }

}
