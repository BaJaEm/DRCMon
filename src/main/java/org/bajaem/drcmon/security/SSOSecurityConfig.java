
package org.bajaem.drcmon.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Sso
@Profile("sso")
public class SSOSecurityConfig extends DRCSecurityConfig implements AuthoritiesExtractor
{
    private final SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");

    private final SimpleGrantedAuthority user = new SimpleGrantedAuthority("ROLE_USER");

    private static final Logger LOG = LogManager.getLogger();

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        LOG.info("SSO Security Enabled");
        super.configure(http);
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.logout().logoutSuccessUrl("/").permitAll();
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(final Map<String, Object> _map)
    {
        final List<GrantedAuthority> ga = new ArrayList<>();
        final List<String> o = (List<String>) _map.get("gevdsGroupIDmemberOf");
        for (final String g : o)
        {
            final SimpleGrantedAuthority sga = new SimpleGrantedAuthority(g);
            ga.add(sga);
        }

        ga.add(admin);
        ga.add(user);
        return ga;
    }

}
