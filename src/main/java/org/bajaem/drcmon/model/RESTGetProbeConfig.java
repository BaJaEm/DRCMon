package org.bajaem.drcmon.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@DiscriminatorValue(value = RESTGetProbeConfig.DV)
@JsonTypeName(RESTGetProbeConfig.DV)
public class RESTGetProbeConfig extends URLBasedConfig
{
    public static final String DV = "RESTGet";
}
