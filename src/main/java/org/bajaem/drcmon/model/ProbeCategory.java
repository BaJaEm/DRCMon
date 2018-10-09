
package org.bajaem.drcmon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "Generator", sequenceName = "key_seq", allocationSize = 1)

public class ProbeCategory
{

    private long id;

    private String name;

    private String channel;

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

    @Column(nullable = false, length = 50, unique = true)
    public String getName()
    {
        return name;

    }

    public void setName(final String _name)
    {
        name = _name;
    }

    @Column(nullable = false, length = 25, unique = true)
    public String getChannel()
    {
        return channel;
    }

    public void setChannel(final String _channel)
    {
        channel = _channel;
    }
}
