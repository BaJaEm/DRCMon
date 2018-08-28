
package org.bajaem.drcmon.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.Configurable;
import org.bajaem.drcmon.model.ProbeType;
import org.bajaem.drcmon.probe.Probe;
import org.bajaem.drcmon.respository.ProbeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

/**
 * Hold meta data about the Probe to Configurable Class relationship.
 *
 * When the bean is created it will scan the class path for the ProbeMarker
 * annotated classes and build a cache of the mappings of following Mappings:
 * <li>Probe Class to corresponding Configurable Class
 * <li>Configurable Class to corresponding Probe Class
 * <li>DiscriminatorValue value String to corresponding Configurable Class
 * <li>Configurable Class to corresponding DiscriminatorValue value String
 *
 */
@Component
public class ProbeMarkerCache
{

    private static final Logger LOG = LogManager.getLogger();

    private final ClassInfoList probeTypes;

    @Autowired
    ProbeTypeRepository repo;

    // Map Probe Class -> Configurable Class
    private final Map<Class<? extends Probe>, Class<? extends Configurable>> probe2Config = new HashMap<>();

    // Map Configurable Class -> Probe Class
    private final Map<Class<? extends Configurable>, Class<? extends Probe>> config2Probe = new HashMap<>();

    private final Map<String, ProbeType> name2pt = new HashMap<>();

    private final Map<String, Class<? extends Probe>> type2Probe = new HashMap<>();

    private final Map<String, Class<? extends Configurable>> type2Config = new HashMap<>();

    private final Map<Class<? extends Configurable>, String> config2TypeName = new HashMap<>();

    private final Map<Class<? extends Configurable>, ProbeType> config2Type = new HashMap<>();

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
                    final Class<? extends Configurable> c = pm.config();
                    final String pt = pm.typeName();
                    probe2Config.put(p, c);
                    config2Probe.put(c, p);
                    type2Probe.put(pt, p);
                    type2Config.put(pt, c);
                    config2TypeName.put(c, pt);
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
     * Get Map of Probe Class to corresponding Configurable Class
     *
     * @return unmodifiableMap of backed by the live map
     */

    public Map<Class<? extends Probe>, Class<? extends Configurable>> getProbe2Config()
    {
        return Collections.unmodifiableMap(probe2Config);
    }

    /**
     * Get Map of Configurable Class to corresponding Probe Class
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<Class<? extends Configurable>, Class<? extends Probe>> getConfig2Probe()
    {
        return Collections.unmodifiableMap(config2Probe);
    }

    /**
     * Get Map of Probe Type name to corresponding Configurable Class
     *
     * @return unmodifiableMap of backed by the live map
     */

    public Map<String, Class<? extends Configurable>> getType2Config()
    {
        return Collections.unmodifiableMap(type2Config);
    }

    /**
     * Get Map of Probe Type name to corresponding Probe Class
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<String, Class<? extends Probe>> getType2Probe()
    {
        return Collections.unmodifiableMap(type2Probe);
    }

    /**
     * Get Map of Probe Type name to corresponding Probe Type
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<String, ProbeType> getProbeTypeName2ProbeType()
    {
        return Collections.unmodifiableMap(name2pt);
    }

    /**
     * Get Map of Probe Type name to corresponding Probe Type
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<Class<? extends Configurable>, ProbeType> getConfig2ProbeType()
    {
        if (name2pt.isEmpty())
        {
            repo.findAll().forEach(t -> name2pt.put(t.getName(), t));
            config2TypeName.forEach((k, v) -> config2Type.put(k, name2pt.get(v)));

        }
        return Collections.unmodifiableMap(config2Type);
    }

    public ProbeType getProbeTypeByConfig(final Class<? extends Configurable> clazz)
    {
        return getConfig2ProbeType().get(clazz);
    }
}
