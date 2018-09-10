
package org.bajaem.drcmon.model;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;

public abstract class URLBasedConfig extends Configurable
{

    public URLBasedConfig(final ProbeConfig _config, final ProbeMarkerCache _cache)
    {
        super(_config, _cache);
    }

    private static final String URL_KEY = "URL";

    private static final String EXPECTED_KEY = "EXPECTED";

    private static final String PATH_KEY = "PATH";

    public String getUrl()
    {
        return getConfig().getCustomConfiguration().get(URL_KEY);
    }

    public void setUrl(final String _url)
    {
        getConfig().getCustomConfiguration().put(URL_KEY, _url);
    }

    public String getExpected()
    {
        return getConfig().getCustomConfiguration().get(EXPECTED_KEY);
    }

    public void setExpected(final String _expected)
    {
        getConfig().getCustomConfiguration().put(EXPECTED_KEY, _expected);
    }

    public String getPath()
    {
        return getConfig().getCustomConfiguration().get(PATH_KEY);
    }

    public void setPath(final String _path)
    {
        getConfig().getCustomConfiguration().put(PATH_KEY, _path);
    }
}
