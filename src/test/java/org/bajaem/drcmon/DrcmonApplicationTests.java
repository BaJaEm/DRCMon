
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.model.ProbeType;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.bajaem.drcmon.respository.ProbeTypeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.security.test.context.support.WithMockUser;

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
        for (final BeanDefinition bd : scanner.findCandidateComponents("*"))
        {
            try
            {
                final Class<?> clazz = Class.forName(bd.getBeanClassName());
                final ProbeMarker m = clazz.getAnnotation(ProbeMarker.class);

                LOG.error(m.name() + m.toString());
            }
            catch (final ClassNotFoundException e)
            {
                LOG.fatal("Could not find class: " + bd.getBeanClassName(), e);
                throw new RuntimeException(e);
            }
        }

        LOG.error("Got Here");
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void data() throws UnknownHostException
    {
        final Calendar now = Calendar.getInstance();
        final Timestamp nowdt = new Timestamp(now.getTimeInMillis());
        final ProbeType type = ptRepo.findByName("SQLQueryProbe");
        assertNotNull(type);
        final ProbeConfig conf = new ProbeConfig();
        conf.setCreatedBy("Glen");
        conf.setCreatedOn(nowdt);
        conf.setDelayTime(0);
        conf.setLastModifiedBy("Glen");
        conf.setLastModifiedOn(nowdt);
        conf.setPollingInterval(30);
        conf.setProbeType(type);
        final Map<String, String> config = new HashMap<>();
        config.put("url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;AUTO_SERVER=true");
        config.put("user", "System");
        config.put("password", "");
        config.put("query", "SELECT name FROM probe_type WHERE name = 'SQLQueryProbe'");
        config.put("expected", "SQLQueryProbe");
        conf.setCustomConfiguration(config);
        pcRepo.save(conf);

    }
}
