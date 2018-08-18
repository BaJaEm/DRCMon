
package org.bajaem.drcmon.configuration;

import org.bajaem.drcmon.security.SystemUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Run arbitrary code ( from the {@link Wrapped} Functional Interface ) as the
 * {@link SystemUser}.
 *
 * Example:
 * 
 * <pre>
 * new SystemUserWrapper().executeAsSystem(() -> eng.init());
 * </pre>
 *
 */
public class SystemUserWrapper
{

    public void executeAsSystem(final Wrapped wrapped)
    {
        try
        {
            final SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(ctx);
            ctx.setAuthentication(SystemUser.get());
            wrapped.execute();
        }
        finally
        {
            SecurityContextHolder.clearContext();
        }
    }
}
