/**
 *
 */

package org.bajaem.drcmon.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.persistence.AttributeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**

 *
 */
public class InetAddressToStringConverter implements AttributeConverter<InetAddress, String>
{

    private static final Logger LOG = LogManager.getLogger(InetAddressToStringConverter.class);

    @Override
    public String convertToDatabaseColumn(final InetAddress value)
    {
        return null != value ? value.getCanonicalHostName() : null;
    }

    @Override
    public InetAddress convertToEntityAttribute(final String value)
    {
        try
        {

            return InetAddress.getByName(value);
        }
        catch (final UnknownHostException e)
        {
            LOG.fatal(e);
            throw new RuntimeException(e);
        }
    }
}
