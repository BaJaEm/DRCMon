
package org.bajaem.drcmon.eventhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.util.SystemClock;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final Logger LOG = LogManager.getLogger();

    @Override
    protected void onBeforeCreate(final ProbeConfig p)
    {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String currentPrincipalName = authentication.getName();

        p.setCreatedOn(SystemClock.currentTime());
        p.setLastModifiedOn(SystemClock.currentTime());
        p.setCreatedBy(currentPrincipalName);
        p.setLastModifiedBy(currentPrincipalName);
        super.onBeforeCreate(p);
    }

    @Override
    protected void onBeforeSave(final ProbeConfig p)
    {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String currentPrincipalName = authentication.getName();
        LOG.trace("updating: " + p.getCreatedOn());

        p.setLastModifiedOn(SystemClock.currentTime());
        p.setLastModifiedBy(currentPrincipalName);
        super.onBeforeSave(p);
    }

}
