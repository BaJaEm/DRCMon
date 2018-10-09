
package org.bajaem.drcmon.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bajaem.drcmon.probe.Configurable;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ProbeMarker
{

    Class<? extends Configurable> config();

    String typeName();
}
