
package org.bajaem.drcmon.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.DiscriminatorValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.probe.Probe;
import org.springframework.stereotype.Component;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

/**
 * Hold meta data about the Probe to ProbeConfiguration relationship.
 * 
 * When the bean is created it will scan the class path for the ProbeMarker
 * annotated classes and build a cache of the mappings of following Mappings:
 * <li>Probe Class to corresponding ProbeConfiguration Class
 * <li>ProbeConfiguration Class to corresponding Probe Class
 * <li>DiscriminatorValue value String to corresponding ProbeConfig Class
 * <li>ProbeConfig Class to corresponding DiscriminatorValue value String
 *
 */
@Component
public class ProbeMarkerCache
{

    private static final Logger LOG = LogManager.getLogger();

    private final ClassInfoList probeTypes;

    // Map Probe Class -> ProbeConfig Class
    private final Map<Class<? extends Probe>, Class<? extends ProbeConfig>> probe2Config = new HashMap<>();

    // Map ProbeConfig Class -> Probe Class
    private final Map<Class<? extends ProbeConfig>, Class<? extends Probe>> config2Probe = new HashMap<>();

    // Map ProbeConfig Class -> DiscriminatorValue value String
    private final Map<String, Class<? extends ProbeConfig>> dv2Config = new HashMap<>();

    // Map DiscriminatorValue value String -> ProbeConfig Class
    private final Map<Class<? extends ProbeConfig>, String> config2Dv = new HashMap<>();

    ProbeMarkerCache()
    {

        LOG.debug("Starting Annotation Scan");
        try (final ScanResult scanResult = new ClassGraph() //
                .enableAnnotationInfo() //
                .whitelistPackages("*") //
                .scan())
        {
            probeTypes = scanResult.getClassesWithAnnotation("org.bajaem.drcmon.configuration.ProbeMarker");

            for (final ClassInfo ci : probeTypes)
            {
                final String className = ci.getName();
                try
                {
                    @SuppressWarnings("unchecked")
                    final Class<? extends Probe> p = (Class<? extends Probe>) Class.forName(className);
                    final ProbeMarker pm = p.getAnnotation(ProbeMarker.class);

                    final Class<? extends ProbeConfig> c = pm.config();
                    final DiscriminatorValue dv = c.getAnnotation(DiscriminatorValue.class);
                    final String d = dv.value();
                    probe2Config.put(p, c);
                    config2Probe.put(c, p);
                    dv2Config.put(d, c);
                    config2Dv.put(c, d);
                }
                catch (final ClassNotFoundException e)
                {
                    LOG.fatal("Could not find class: " + className, e);
                    throw new RuntimeException(e);
                }
            }
        }
        LOG.debug("Completed Annotation Scan");

    }

    /**
     * Get Map of Probe Class to corresponding ProbeConfiguration Class
     * 
     * @return unmodifiableMap of backed by the live map
     */

    public Map<Class<? extends Probe>, Class<? extends ProbeConfig>> getProbe2Config()
    {
        return Collections.unmodifiableMap(probe2Config);
    }

    /**
     * Get Map of ProbeConfiguration Class to corresponding Probe Class
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<Class<? extends ProbeConfig>, Class<? extends Probe>> getConfig2Probe()
    {
        return Collections.unmodifiableMap(config2Probe);
    }

    /**
     * Get Map of DiscriminatorValue value String to corresponding ProbeConfig
     * Class
     * 
     * @return unmodifiableMap of backed by the live map
     */
    public Map<String, Class<? extends ProbeConfig>> getDv2Config()
    {
        return Collections.unmodifiableMap(dv2Config);
    }

    /**
     * Get Map of ProbeConfig Class to corresponding DiscriminatorValue value
     * String
     * 
     * @return unmodifiableMap of backed by the live map
     */
    public Map<Class<? extends ProbeConfig>, String> getConfig2Dv()
    {
        return Collections.unmodifiableMap(config2Dv);
    }

}
