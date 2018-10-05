
package org.bajaem.drcmon.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.bajaem.drcmon.util.SystemClock;
import org.bajaem.drcmon.util.converters.BooleanToStringConverter;
import org.bajaem.drcmon.util.converters.MapToStringConverter;

@Entity
@SequenceGenerator(name = "Generator", sequenceName = "key_seq", allocationSize = 1)
public class ProbeConfig
{

    private long id;

    private String artifactId;

    private int pollingInterval;

    private int delayTime;

    private Timestamp createdOn;

    private String createdBy;

    private Timestamp lastModifiedOn;

    private String lastModifiedBy;

    private boolean enabled;

    private ProbeType probeType;

    private String label;

    private Map<String, String> customConfiguration = new HashMap<>();

    private ProbeKey probeKey;

    private ProbeCategory probeCategory;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Generator")
    public long getId()
    {
        return id;
    }

    public void setId(final long _id)
    {
        id = _id;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId(final String _artifactId)
    {
        artifactId = _artifactId;
    }

    public int getPollingInterval()
    {
        return pollingInterval;
    }

    public void setPollingInterval(final int _pollingInterval)
    {
        pollingInterval = _pollingInterval;
    }

    public int getDelayTime()
    {
        return delayTime;
    }

    public void setDelayTime(final int _delayTime)
    {
        delayTime = _delayTime;
    }

    @Column(name = "created_on", nullable = false, updatable = false)
    public Timestamp getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(final Timestamp _createdOn)
    {
        if (_createdOn == null)
        {
            createdOn = SystemClock.currentTime();
        }
        createdOn = _createdOn;
    }

    @Column(name = "created_by", nullable = false, updatable = false)
    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(final String _createdBy)
    {
        createdBy = _createdBy;
    }

    public Timestamp getLastModifiedOn()
    {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(final Timestamp _lastModifiedOn)
    {
        lastModifiedOn = _lastModifiedOn;
    }

    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final String _lastModifiedBy)
    {
        lastModifiedBy = _lastModifiedBy;
    }

    @Convert(converter = MapToStringConverter.class)
    public Map<String, String> getCustomConfiguration()
    {
        return customConfiguration;
    }

    public void setCustomConfiguration(final Map<String, String> _customConfiguration)
    {
        customConfiguration = _customConfiguration;
    }

    @Convert(converter = BooleanToStringConverter.class)
    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(final boolean _enabled)
    {
        enabled = _enabled;
    }

    @ManyToOne
    @JoinColumn(name = "probe_type")
    public ProbeType getProbeType()
    {
        return probeType;
    }

    public void setProbeType(final ProbeType _probeType)
    {
        probeType = _probeType;
    }

    @ManyToOne
    @JoinColumn(name = "probe_key")
    public ProbeKey getProbeKey()
    {
        return probeKey;
    }

    public void setProbeKey(final ProbeKey _probeKey)
    {
        probeKey = _probeKey;
    }

    @ManyToOne
    @JoinColumn(name = "probe_category")
    public ProbeCategory getProbeCategory()
    {
        return probeCategory;
    }

    public void setProbeCategory(final ProbeCategory _probeCategory)
    {
        probeCategory = _probeCategory;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(final String _label)
    {
        label = _label;
    }

    @Override
    public String toString()
    {
        return id + " - " + artifactId + " - " + " - " + pollingInterval + " - " + delayTime + " - " + createdOn + " - "
                + createdBy + " - " + lastModifiedOn + " - " + lastModifiedBy;
    }

}
