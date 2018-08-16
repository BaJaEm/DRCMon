
package org.bajaem.drcmon.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

/**
 * System user with Administrative privileges. Any access to the JPA
 * Repositories require a {@link SecurityContext} for execution. This class
 * provides a system user that can be used for internal application calls.
 * 
 */
public class SystemUser extends AnonymousAuthenticationToken
{

    private static final long serialVersionUID = 4012979444608706936L;

    private static SystemUser systemUser;

    private SystemUser()
    {
        super("none", "none", Collections.unmodifiableCollection(Arrays.asList(new SimpleGrantedAuthority[] {
                new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER") })));
    }

    /**
     * Get the instance of the SystemUser
     * 
     * @return the singleton of the SystemUser.
     */
    public static SystemUser get()
    {
        synchronized (SystemUser.class)
        {
            if (null == systemUser)
            {
                systemUser = new SystemUser();
            }
        }
        return systemUser;
    }

}
