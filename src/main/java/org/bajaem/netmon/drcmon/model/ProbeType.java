package org.bajaem.netmon.drcmon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "Generator", sequenceName = "key_seq",  allocationSize=1)
public class ProbeType
{

    private String name;

    private String description;

    private long id;

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

    public void setName(final String _name)
    {
        name = _name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String _description)
    {
        description = _description;
    }

}
