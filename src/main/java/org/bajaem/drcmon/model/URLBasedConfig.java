package org.bajaem.drcmon.model;

public abstract class URLBasedConfig extends ProbeConfig
{

    private static final String URL_KEY = "URL";

    private static final String EXPECTED_KEY = "EXPECTED";

    private static final String PATH_KEY = "PATH";

    private static final String KEYFILE_KEY = "KEYFILE";

    public String getUrl()
    {
        return getCustomConfiguration().get(URL_KEY);
    }

    public void setUrl(final String _url)
    {
        getCustomConfiguration().put(URL_KEY, _url);
    }

    public String getExpected()
    {
        return getCustomConfiguration().get(EXPECTED_KEY);
    }

    public void setExpected(final String _expected)
    {
        getCustomConfiguration().put(EXPECTED_KEY, _expected);
    }

    public String getPath()
    {
        return getCustomConfiguration().get(PATH_KEY);
    }

    public void setPath(final String _path)
    {
        getCustomConfiguration().put(PATH_KEY, _path);
    }

    public String getKeyFile()
    {
        return getCustomConfiguration().get(KEYFILE_KEY);
    }

    public void setKeyFile(final String _keyFile)
    {
        getCustomConfiguration().put(KEYFILE_KEY, _keyFile);
    }

}
