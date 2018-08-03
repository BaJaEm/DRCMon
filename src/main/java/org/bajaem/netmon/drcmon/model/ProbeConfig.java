package org.bajaem.netmon.drcmon.model;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.bajaem.netmon.drcmon.util.InetAddressToStringConverter;
import org.bajaem.netmon.drcmon.util.MapToStringConverter;

@Entity
@SequenceGenerator(name = "Generator", sequenceName = "key_seq", allocationSize = 1)
public class ProbeConfig
{

    private long id;

    private String artifactId;

    private ProbeType probeType;

    private InetAddress host;

    private int pollingInterval;

    private int delayTime;

    private Timestamp createdOn;

    private String createdBy;

    private Timestamp lastModifiedOn;

    private String lastModifiedBy;

    private Map<String, String> customConfiguration;

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

    @ManyToOne
    @JoinColumn(name = "monitor_type_id")
    public ProbeType getProbeType()
    {
        return probeType;
    }

    public void setProbeType(final ProbeType _probeType)
    {
        probeType = _probeType;
    }

    @Convert(converter = InetAddressToStringConverter.class)
    public InetAddress getHost()
    {
        return host;
    }

    public void setHost(final InetAddress _host)
    {
        host = _host;
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

    public Timestamp getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(final Timestamp _createdOn)
    {
        createdOn = _createdOn;
    }

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

    public String toString()
    {
        return id + " - " + artifactId + " - " + probeType + " - " + host + " - " + pollingInterval + " - " + delayTime
                + " - " + createdOn + " - " + createdBy + " - " + lastModifiedOn + " - " + lastModifiedBy;
    }

}
