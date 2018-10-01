
package org.bajaem.drcmon.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class DRCSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        http.authorizeRequests().antMatchers("/api/**").permitAll();
        http.authorizeRequests().antMatchers("/monitor").permitAll();
        http.authorizeRequests().antMatchers("/login/**").permitAll();
        http.authorizeRequests().antMatchers("/index.html").permitAll();
        http.authorizeRequests().antMatchers("/**").authenticated();
    }
}
