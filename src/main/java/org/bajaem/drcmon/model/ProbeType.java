
package org.bajaem.drcmon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@SequenceGenerator(name = "Generator", sequenceName = "key_seq", allocationSize = 1)
public class ProbeType
{

    private long id;

    private String name;

    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Generator")
    public long getId()
    {
        return id;
    }

    public void setId(final long _id)
    {
        id = _id;
    }

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

    public String configClassName()
    {
        return "Test";
    }

}
