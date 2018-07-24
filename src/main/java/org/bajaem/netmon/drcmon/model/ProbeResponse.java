package org.bajaem.netmon.drcmon.model;

import java.sql.Date;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.util.BooleanToStringConverter;

@Entity
@SequenceGenerator(name = "Generator", sequenceName = "key_seq",  allocationSize=1)
public class ProbeResponse
{
    private static final Logger LOG = LogManager.getLogger(ProbeResponse.class);


    private long id;

    private ProbeConfig probeConfig;

    private Date startTime;

    private Date endTime;

    private boolean success;

    private String errorMessage;

    private String error;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Generator")
    public long getId()
    {
        return id;
    }

    public void setId(final long _id)
    {
        LOG.info("My ID: " + id);
        id = _id;
    }

    @ManyToOne
    @JoinColumn(name = "probe_config")
    public ProbeConfig getProbeConfig()
    {
        return probeConfig;
    }

    public void setProbeConfig(final ProbeConfig _probeConfig)
    {
        probeConfig = _probeConfig;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(final Date _startTime)
    {
        startTime = _startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(final Date _endTime)
    {
        endTime = _endTime;
    }

    @Convert(converter = BooleanToStringConverter.class)
    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(final boolean _success)
    {
        success = _success;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(final String _errorMessage)
    {
        errorMessage = _errorMessage;
    }

    public String getError()
    {
        return error;
    }

    public void setError(final String _error)
    {
        error = _error;
    }

}
