package org.bajaem.drcmon.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "SQLQuery")
public class SQLQueryProbeConfig extends URLBasedConfig
{

    private static final String QUERY_KEY = "QUERY";

    @Transient
    public String getQuery()
    {
        return getCustomConfiguration().get(QUERY_KEY);
    }

    public void setQuery(final String _query)
    {
        getCustomConfiguration().put(QUERY_KEY, _query);
    }
}
