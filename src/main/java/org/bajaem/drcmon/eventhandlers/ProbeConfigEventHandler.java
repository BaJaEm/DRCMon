package org.bajaem.drcmon.eventhandlers;

import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.util.SystemClock;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/**
 * Set the last modified time and created time on creation - we don't want to
 * rely on the client to set these values so we set them ourselves. The same is
 * the case for setting the last modified time for any updates.
 *
 */
@Component
public class ProbeConfigEventHandler extends AbstractRepositoryEventListener<ProbeConfig>
{

    @Override
    protected void onBeforeCreate(final ProbeConfig p)
    {
        p.setCreatedOn(SystemClock.currentTime());
        p.setLastModifiedOn(SystemClock.currentTime());
        super.onBeforeCreate(p);
    }

    @Override
    protected void onBeforeSave(final ProbeConfig p)
    {
        p.setLastModifiedOn(SystemClock.currentTime());
        super.onBeforeSave(p);
    }

}
