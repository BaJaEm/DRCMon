
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
 * SystemUserWrapper.executeAsSystem(() -> eng.init());
 * </pre>
 *
 */
public class SystemUserWrapper
{

    public static void executeAsSystem(final Wrapped wrapped)
    {
        new SystemUserWrapper().executeAsSystemHelper(wrapped);
    }

    public static <T> T executeAsSystem(final TypedWrapped<T> wrapped)
    {
        return new SystemUserWrapper().executeAsSystemHelper(wrapped);
    }

    private void executeAsSystemHelper(final Wrapped wrapped)
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

    private <T> T executeAsSystemHelper(final TypedWrapped<T> wrapped)
    {
        try
        {
            final SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(ctx);
            ctx.setAuthentication(SystemUser.get());
            return wrapped.execute();
        }
        finally
        {
            SecurityContextHolder.clearContext();
        }
    }
}
