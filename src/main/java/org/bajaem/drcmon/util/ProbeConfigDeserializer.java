
package org.bajaem.drcmon.util;

import java.io.IOException;

import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.model.ProbeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;


public class ProbeConfigDeserializer<T extends ProbeConfig> extends StdDeserializer<T>
{

    @Autowired
    ProbeMarkerCache cache;
    
    private static final long serialVersionUID = 509501269440915407L;

    public ProbeConfigDeserializer(final Class<T> vc)
    {
        super(vc);
    }

    @Override
    public T deserialize(final JsonParser _p, final DeserializationContext _ctxt)
            throws IOException, JsonProcessingException
    {
        
        return null;
    }

}
