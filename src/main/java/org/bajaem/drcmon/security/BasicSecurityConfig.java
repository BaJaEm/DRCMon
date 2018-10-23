
package org.bajaem.drcmon.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.SystemUserWrapper;
import org.bajaem.drcmon.configuration.TypedWrapped;
import org.bajaem.drcmon.configuration.Wrapped;
import org.bajaem.drcmon.model.ProbeUser;
import org.bajaem.drcmon.respository.ProbeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!sso")
public class BasicSecurityConfig extends DRCSecurityConfig
{

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private ProbeUserRepository userRepo;

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        LOG.info("Basic Security Enabled");
        super.configure(http);
        http.httpBasic();
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception
    {
        final TypedWrapped<Iterable<ProbeUser>> w = () -> userRepo.findAll();
        final Iterable<ProbeUser> users = SystemUserWrapper.executeAsSystem(w);
        for (final ProbeUser s : users)
        {
            final String user = s.getUserId();
            if (s.isAdmin())
            {
                auth.inMemoryAuthentication().withUser(user).password("{noop}" + s.getSecret()).roles("ADMIN", "USER");
            }
            else
            {
                auth.inMemoryAuthentication().withUser(user).password("{noop}" + s.getSecret()).roles("USER");
            }
        }
    }
}