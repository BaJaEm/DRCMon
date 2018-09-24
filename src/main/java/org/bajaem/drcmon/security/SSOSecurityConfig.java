
package org.bajaem.drcmon.security;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Sso
@Profile("sso")
public class SSOSecurityConfig extends DRCSecurityConfig implements PrincipalExtractor, AuthoritiesExtractor
{

    private final SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");

    private final List<? extends GrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority[] { admin });

    private static final Logger LOG = LogManager.getLogger();

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        LOG.info("SSO Security Enabled");
        super.configure(http);
    }

    @Autowired
    public void foo()
    {
        LOG.info("Starting foo");
        // Collection<SimpleGrantedAuthority> oldAuthorities =
        // (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // SimpleGrantedAuthority authority = new
        // SimpleGrantedAuthority("ROLE_ANOTHER");
        // List<SimpleGrantedAuthority> updatedAuthorities = new
        // ArrayList<SimpleGrantedAuthority>();
        // updatedAuthorities.add(authority);
        // updatedAuthorities.addAll(oldAuthorities);
        //
        // SecurityContextHolder.getContext().setAuthentication(
        // new UsernamePasswordAuthenticationToken(
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
        // SecurityContextHolder.getContext().getAuthentication().getCredentials(),
        // updatedAuthorities)
        // );

    }

    @Override
    public Object extractPrincipal(final Map<String, Object> _map)
    {
//        final String id = (String) _map.get("id");
//        _map.keySet().forEach(k -> LOG.info(k + " => " + _map.get(k)));
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        final Collection authorities = authentication.getAuthorities();
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return authentication;
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> _map)
    {
        _map.keySet().forEach(k -> LOG.info(k + " => " + _map.get(k)));
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("AUTH: " + authentication);
        return null;
    }

}
