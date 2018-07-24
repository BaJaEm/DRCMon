package org.bajaem.netmon.drcmon.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String>
{

    @Override
    public String convertToDatabaseColumn(final Boolean value)
    {
        return (value != null && value) ? "T" : "F";
    }

    @Override
    public Boolean convertToEntityAttribute(final String value)
    {
        return "T".equals(value);
    }
}
