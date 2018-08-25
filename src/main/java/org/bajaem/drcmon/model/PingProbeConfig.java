
package org.bajaem.drcmon.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue(value = PingProbeConfig.DV)
// @JsonTypeName(PingProbeConfig.DV)
public class PingProbeConfig extends HostProbeConfig
{

    public static final String DV = "Ping";

    public PingProbeConfig()
    {
        super();
    }

    @Transient
    @JsonIgnore
    public InetAddress getInetAddress() throws UnknownHostException
    {
        return InetAddress.getByName(getHost());
    }
}