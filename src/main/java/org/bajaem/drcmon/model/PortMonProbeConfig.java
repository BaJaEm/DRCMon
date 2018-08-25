package org.bajaem.drcmon.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@DiscriminatorValue(value = PortMonProbeConfig.DV)
@JsonTypeName(PortMonProbeConfig.DV)
public class PortMonProbeConfig extends HostProbeConfig
{
    public static final String DV = "PortMon";

    private static final String PORT_KEY = "PORT";

    @Transient
    @JsonIgnore
    public Integer getPort()
    {
        return Integer.parseInt(getCustomConfiguration().get(PORT_KEY));
    }

    public void setPort(final Integer port)
    {
        getCustomConfiguration().put(PORT_KEY, port.toString());
    }
}
