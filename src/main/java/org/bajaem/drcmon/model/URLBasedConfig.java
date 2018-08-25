
package org.bajaem.drcmon.model;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class URLBasedConfig extends ProbeConfig
{

    private static final String URL_KEY = "URL";

    private static final String EXPECTED_KEY = "EXPECTED";

    private static final String PATH_KEY = "PATH";

    private static final String KEYFILE_KEY = "KEYFILE";

    @JsonIgnore
    @Transient
    public String getUrl()
    {
        return getCustomConfiguration().get(URL_KEY);
    }

    @JsonIgnore
    @Transient
    public void setUrl(final String _url)
    {
        getCustomConfiguration().put(URL_KEY, _url);
    }

    @JsonIgnore
    @Transient
    public String getExpected()
    {
        return getCustomConfiguration().get(EXPECTED_KEY);
    }

    @JsonIgnore
    @Transient
    public void setExpected(final String _expected)
    {
        getCustomConfiguration().put(EXPECTED_KEY, _expected);
    }

    @JsonIgnore
    @Transient
    public String getPath()
    {
        return getCustomConfiguration().get(PATH_KEY);
    }

    @JsonIgnore
    @Transient
    public void setPath(final String _path)
    {
        getCustomConfiguration().put(PATH_KEY, _path);
    }

    @JsonIgnore
    @Transient
    public String getKeyFile()
    {
        return getCustomConfiguration().get(KEYFILE_KEY);
    }

    @JsonIgnore
    @Transient
    public void setKeyFile(final String _keyFile)
    {
        getCustomConfiguration().put(KEYFILE_KEY, _keyFile);
    }

}
