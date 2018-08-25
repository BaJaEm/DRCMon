
package org.bajaem.drcmon.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.exceptions.DRCProbeException;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Simple Web Client interface to make GET/POST/PUT/DELETE requests to a URL
 * Note that any authentication will need to be handled by the implementing
 * class.
 *
 */
public abstract class DRCWebClient
{

    private static final Logger LOG = LogManager.getLogger(DRCWebClient.class);

    protected final HttpHeaders requestHeaders;

    protected final URL url;

    protected final HttpHost host;

    /**
     * Base Constructor for DRCWebClient
     *
     * @param _url
     *            URL to access
     * @param _headers
     *            all headers to be included in the request
     */
    public DRCWebClient(final String _url, final Map<String, String> _headers)
    {
        requestHeaders = new HttpHeaders();
        for (final String key : _headers.keySet())
        {
            requestHeaders.add(key, _headers.get(key));
        }
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        try
        {

            url = new URL(_url);
        }
        catch (final MalformedURLException e)
        {
            LOG.fatal(e);
            throw new DRCStartupException(e);
        }
        final String uHost = url.getHost();
        final int uPort = url.getPort();
        final int uDefPort = url.getDefaultPort();
        final String uProto = url.getProtocol();
        host = new HttpHost(uHost, uPort == -1 ? uDefPort : uPort, uProto);
    }

    public String getHost()
    {
        return url.getHost();
    }

    public final JsonNode get() throws DRCProbeException
    {
        return get(JsonNode.class);
    }

    public final JsonNode delete() throws DRCProbeException
    {
        return delete(JsonNode.class);
    }

    public final JsonNode post(final String body) throws DRCProbeException
    {
        return post(JsonNode.class, body);
    }

    public final JsonNode post() throws DRCProbeException
    {
        return post(null);
    }

    public final JsonNode put(final String body) throws DRCProbeException
    {
        return put(JsonNode.class, body);
    }

    public <T> T get(final Class<T> type) throws DRCProbeException
    {
        return action(type, null, HttpMethod.GET);
    }

    public <T> T delete(final Class<T> type) throws DRCProbeException
    {
        return action(type, null, HttpMethod.DELETE);
    }

    public <T> T post(final Class<T> type, final String body) throws DRCProbeException
    {
        return action(type, body, HttpMethod.POST);
    }

    public <T> T put(final Class<T> type, final String body) throws DRCProbeException
    {
        return action(type, body, HttpMethod.PUT);
    }

    /**
     * Contract to execute a web request of the specified type.
     *
     * @param type
     *            Return type of the object requests - it it assumed that the
     *            retun value will be in some text format that can be cast to a
     *            {@link String}, or is JSON and can returned as raw JSON, or be
     *            parsed by a JSON parser to be returned as POJO
     * @param body
     *            Data sent as the request body
     * @param method
     *            HTTP request method to be performed see {@link HttpMethod}
     * @return body of the request
     */
    public abstract <T> T action(final Class<T> type, final String body, final HttpMethod method)
            throws DRCProbeException;

}
