
package org.bajaem.drcmon.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class ProbeType
{

    private String name;

    private String description;

    @Id
    public String getName()
    {
        return name;
    }

    protected void setName(final String _name)
    {
        name = _name;
    }

    public String getDescription()
    {
        return description;
    }

    protected void setDescription(final String _description)
    {
        description = _description;
    }

}
