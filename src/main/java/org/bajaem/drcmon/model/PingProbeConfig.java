package org.bajaem.drcmon.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "Ping")
public class PingProbeConfig extends HostProbeConfig
{
    public PingProbeConfig()
    {
        super();
    }

    @Transient
    public InetAddress getInetAddress() throws UnknownHostException
    {
        return InetAddress.getByName(getHost());
    }
}