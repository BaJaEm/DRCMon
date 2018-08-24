
package org.bajaem.drcmon.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@DiscriminatorValue(value = "Ping")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "probeType", visible = true)
@JsonTypeName("Ping")
public class PingProbeConfig extends HostProbeConfig
{

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