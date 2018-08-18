package org.bajaem.drcmon.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "PortMon")
public class PortMonProbeConfig extends HostProbeConfig
{

    private static final String PORT_KEY = "PORT";

    @Transient
    public Integer getPort()
    {
        return Integer.parseInt(getCustomConfiguration().get(PORT_KEY));
    }

    public void setPort(final Integer port)
    {
        getCustomConfiguration().put(PORT_KEY, port.toString());
    }
}
