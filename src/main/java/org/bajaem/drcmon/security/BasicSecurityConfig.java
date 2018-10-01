
package org.bajaem.drcmon.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
        auth.inMemoryAuthentication().withUser("a").password("{noop}a").roles("USER").and().withUser("b")
                .password("{noop}b").roles("ADMIN", "USER");
    }
}