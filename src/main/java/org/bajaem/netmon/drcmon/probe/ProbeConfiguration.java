package org.bajaem.netmon.drcmon.probe;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "probes")
@PropertySource("classpath:/config/probes.properties")
public class ProbeConfiguration
{

    private final List<String> ping = new ArrayList<>();

    public List<String> getPing()
    {
        return ping;
    }

    public Map<String, Probe> getProbes()
    {
        final Map<String, Probe> probes = new HashMap<>();
        try
        {
            for (final String pinger : getPing())
            {
                probes.put(pinger, new PingProbe(10, InetAddress.getByName(pinger)));
            }
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException();
        }
        return probes;
    }
}
