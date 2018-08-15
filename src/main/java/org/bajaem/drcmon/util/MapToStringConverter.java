
package org.bajaem.drcmon.util;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//TODO: can we make this use Beans rather than a map?  They would need to be dynamic
public class MapToStringConverter implements AttributeConverter<Map<String, String>, String>
{

    private static final Logger LOG = LogManager.getLogger(MapToStringConverter.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final Map<String, String> _attribute)
    {
        try
        {
            return mapper.writeValueAsString(_attribute);
        }
        catch (final JsonProcessingException e)
        {
            LOG.fatal("Configuraion Error.  Could not serialize object: " + _attribute, e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> convertToEntityAttribute(final String _dbData)
    {
        try
        {

            return mapper.readValue(_dbData, Map.class);
        }
        catch (final IOException e)
        {
            LOG.fatal("Configuraion Error.  Could not serialize object: " + _dbData, e);
            throw new RuntimeException(e);
        }
    }

}
