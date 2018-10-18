
package org.bajaem.drcmon.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.ProbeType;
import org.bajaem.drcmon.probe.Configurable;
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
 * annotated classes and build a cache of the mappings.
 *
 */
@Component
public class ProbeMarkerCache
{

    private static final Logger LOG = LogManager.getLogger();

    private final ClassInfoList probeTypes;

    @Autowired
    private ProbeTypeRepository repo;

    private final Map<String, ProbeType> name2pt = new HashMap<>();

    private final Map<String, Class<? extends Probe>> type2Probe = new HashMap<>();

    private final Map<String, Class<? extends Configurable>> type2Config = new HashMap<>();

    private final Map<Class<? extends Configurable>, String> config2TypeName = new HashMap<>();

    private final Map<Class<? extends Configurable>, ProbeType> config2Type = new HashMap<>();

    private final Map<Class<? extends Probe>, String> probe2Type = new HashMap<>();

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
                    type2Probe.put(pt, p);
                    probe2Type.put(p, pt);
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
     * Get Map of {@link ProbeType} name to corresponding {@link Configurable}
     * class.
     *
     * @return unmodifiableMap of backed by the live map
     */

    public Map<String, Class<? extends Configurable>> getType2Config()
    {
        return Collections.unmodifiableMap(type2Config);
    }

    /**
     * Get Map of {@link ProbeType} name to corresponding {@link Probe} class.
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<String, Class<? extends Probe>> getType2Probe()
    {
        return Collections.unmodifiableMap(type2Probe);
    }

    /**
     * Get Map of {@link Probe} class to corresponding {@link ProbeType} name.
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<Class<? extends Probe>, String> getProbe2Type()
    {
        return Collections.unmodifiableMap(probe2Type);
    }

    /**
     * Get Map of {@link ProbeType} name to corresponding {@link ProbeType}
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<String, ProbeType> getProbeTypeName2ProbeType()
    {
        return Collections.unmodifiableMap(name2pt);
    }

    /**
     * Get Map of {@link Configurable} class to corresponding {@link ProbeType}
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<Class<? extends Configurable>, ProbeType> getConfig2ProbeType()
    {
        if (config2Type.isEmpty())
        {
            final Map<String, ProbeType> pts = getName2ProbeType();
            config2TypeName.forEach((k, v) -> config2Type.put(k, pts.get(v)));
        }
        return Collections.unmodifiableMap(config2Type);
    }

    /**
     * Get {@link ProbeType} by corresponding {@link Configurable}.
     *
     * @return the {@link ProbeType} object, null if there is no corresponding
     *         {@link Configurable}.
     */
    public ProbeType getProbeTypeByConfig(final Class<? extends Configurable> clazz)
    {
        return getConfig2ProbeType().get(clazz);
    }

    /**
     * Get Map of {@link ProbeType} name to corresponding {@link ProbeType}
     *
     * @return unmodifiableMap of backed by the live map
     */
    public Map<String, ProbeType> getName2ProbeType()
    {
        if (name2pt.isEmpty())
        {
            repo.findAll().forEach(t -> name2pt.put(t.getName(), t));
        }
        return Collections.unmodifiableMap(name2pt);
    }

    /**
     * Get the {@link ProbeType} by it's name.
     *
     * @param name
     *            of the probe
     * @return {@link ProbeType} object.
     */
    public ProbeType getProbeTypeByName(final String name)
    {
        return getName2ProbeType().get(name);
    }

    /**
     * Get the name of the probe type by it's Probe Class.
     *
     * @param {@link
     *            ProbeType} object
     * @return name of the probe.
     */
    public String getNameByProbeType(final Class<? extends Probe> pt)
    {
        return getProbe2Type().get(pt);
    }

}
