
package org.bajaem.drcmon.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.mq.MessageSender;

public class PingProbeConfig extends HostProbeConfig
{

    public PingProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache, final MessageSender _sender)
    {
        super(_config, _cache, _sender);
    }

    public InetAddress getInetAddress() throws UnknownHostException
    {
        return InetAddress.getByName(getHost());
    }
}