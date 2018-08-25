package org.bajaem.drcmon.util.converters;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.AttributeConverter;

public class AnyDateToCurrentDateConverter implements AttributeConverter<Timestamp, Timestamp>
{
    @Override
    public Timestamp convertToDatabaseColumn(final Timestamp value)
    {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public Timestamp convertToEntityAttribute(final Timestamp value)
    {
        return value;
    }
}
