package org.bajaem.netmon.drcmon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "config")
@PropertySource("classpath:/config/probes.properties")
public class DRCTestConfig
{

    private final List<String> list = new ArrayList<>();

    public List<String> getList()
    {
        return list;
    }

    private final Map<String, String> map = new HashMap<>();

    public Map<String, String> getMap()
    {
        return map;
    }

}
