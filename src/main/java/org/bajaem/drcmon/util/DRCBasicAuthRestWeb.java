
package org.bajaem.drcmon.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.springframework.util.Base64Utils;

public class DRCBasicAuthRestWeb extends DRCRestTemplate
{

    private static final Logger LOG = LogManager.getLogger(DRCBasicAuthRestWeb.class);

    public DRCBasicAuthRestWeb(final String _url, final String _username, final String _password)
    {
        this(_url, new HashMap<String, String>(), _username, _password);

    }

    public DRCBasicAuthRestWeb(final String _url, final Map<String, String> _headers, final String _username,
            final String _password)
    {
        super(_url, _headers);
        try
        {
            final String token = "Basic " + Base64Utils.encodeToString((_username + ":" + _password).getBytes("UTF-8"));
            requestHeaders.add("Authorization", token);

        }
        catch (final UnsupportedEncodingException e)
        {
            LOG.error(e);
            throw new DRCStartupException(e);
        }

    }
}