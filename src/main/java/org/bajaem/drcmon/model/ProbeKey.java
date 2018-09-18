
package org.bajaem.drcmon.model;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.bajaem.drcmon.util.converters.EncryptionConverter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@SequenceGenerator(name = "Generator", sequenceName = "key_seq", allocationSize = 1)
public class ProbeKey
{

    private long id;

    private String name;

    private String userId;

    private String secret;

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

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(final String _userId)
    {
        userId = _userId;
    }

    @Convert(converter = EncryptionConverter.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getSecret()
    {
        return secret;
    }

    public void setSecret(final String _secret)
    {
        secret = _secret;
    }
}
