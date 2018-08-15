
package org.bajaem.drcmon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {

        http.authorizeRequests().antMatchers("/api/**").permitAll().and().httpBasic();
        http.authorizeRequests().antMatchers("/**").authenticated();
        http.csrf().disable();

        // http.authorizeRequests().antMatchers("/login").permitAll().and().formLogin();
        // http.authorizeRequests().regexMatchers(".*").authenticated();
        // http.csrf().disable();
        // // http.formLogin().loginPage("/login").permitAll();
        // http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login").permitAll()
        // .failureUrl("/login-error").permitAll();
        // http.csrf().disable();
        // http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.POST,
        // "/**").permitAll();
        // .antMatchers("/user/**").hasRole("USER").and().formLogin().loginPage("/login")
        // .failureUrl("/login-error");
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication().withUser("a").password("{noop}a").roles("USER").and().withUser("b")
                .password("{noop}b").roles("ADMIN", "USER");
    }

}
