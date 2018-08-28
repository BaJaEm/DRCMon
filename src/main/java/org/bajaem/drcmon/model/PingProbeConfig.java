
package org.bajaem.drcmon.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;

public class PingProbeConfig extends HostProbeConfig
{

    public PingProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache)
    {
        super(_config, _cache);
    }

    public InetAddress getInetAddress() throws UnknownHostException
    {
        return InetAddress.getByName(getHost());
    }
}