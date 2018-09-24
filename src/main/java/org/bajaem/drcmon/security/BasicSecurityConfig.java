
package org.bajaem.drcmon.security;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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

    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication().withUser("a").password("{noop}a").roles("USER").and().withUser("b")
                .password("{noop}b").roles("ADMIN", "USER");
    }
    
    @Autowired
    public void foo()
    {
        LOG.info("Starting foo");
//        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ANOTHER");
//        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
//        updatedAuthorities.add(authority);
//        updatedAuthorities.addAll(oldAuthorities);
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(
//                        SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
//                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
//                        updatedAuthorities)
//        );

    }
}