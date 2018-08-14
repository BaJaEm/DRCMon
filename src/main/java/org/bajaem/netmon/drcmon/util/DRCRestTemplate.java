package org.bajaem.netmon.drcmon.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.exceptions.DRCStartupException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class DRCRestTemplate extends DRCWebClient
{
    private static final Logger LOG = LogManager.getLogger(DRCRestTemplate.class);

    private final RestTemplate restTemplate;

    private final SSLContext sslContext;

    private final HttpClient client;

    private final HttpComponentsClientHttpRequestFactory requestFactory;

    public DRCRestTemplate(final String _url)
    {
        this(_url, new HashMap<String, String>());
    }

    public DRCRestTemplate(final String _url, final Map<String, String> _headers)
    {
        super(_url, _headers);
        try
        {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
            client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .build();
            requestFactory = new HttpComponentsClientHttpRequestFactory(client);
            restTemplate = new RestTemplate(requestFactory);
        }
        catch (final KeyManagementException | NoSuchAlgorithmException | KeyStoreException e)
        {
            LOG.fatal(e);
            throw new DRCStartupException(e);
        }

    }

    @Override
    public <T> T action(Class<T> _type, String _body, HttpMethod _method)
    {

        final HttpEntity<?> entity;

        if (null != _body)
        {
            entity = new HttpEntity<>(_body, requestHeaders);
        }
        else
        {
            entity = new HttpEntity<T>(requestHeaders);
        }

        try
        {
            final ResponseEntity<T> resp = restTemplate.exchange(url.toString(), _method, entity, _type);
            if (null != resp && null != resp.getBody())
            {
                final T node = resp.getBody();
                return node;
            }
        }
        catch (final Exception e)
        {
            if (e instanceof HttpStatusCodeException)
            {
                final HttpStatusCodeException new_name = (HttpStatusCodeException) e;
                LOG.error(new_name.getResponseBodyAsString());
                LOG.error(new_name.getResponseHeaders());
            }
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

}
