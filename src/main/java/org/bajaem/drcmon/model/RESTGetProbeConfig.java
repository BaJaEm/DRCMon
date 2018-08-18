package org.bajaem.drcmon.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "RESTGet")
public class RESTGetProbeConfig extends URLBasedConfig
{

}
