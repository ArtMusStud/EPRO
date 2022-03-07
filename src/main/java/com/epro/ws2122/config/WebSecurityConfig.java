package com.epro.ws2122.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String READ_ONLY_USER = "Read Only User";
    public static final String CO_OKR_ADMIN = "CO OKR Admin";
    public static final String BUO_OKR_ADMIN = "BUO OKR Admin";

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /* ToDo:
            - BUO OKR Admins dürfen nur ihre eigenen Resourcen bearbeiten
         */
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/company-objectives/**").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.GET, "/company-objectives").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.PATCH, "/company-objectives/**").hasRole(CO_OKR_ADMIN)
                .antMatchers(HttpMethod.POST, "/company-objectives").hasRole(CO_OKR_ADMIN)
                .antMatchers(HttpMethod.PUT, "/company-objectives/**").hasRole(CO_OKR_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/company-objectives/**").hasRole(CO_OKR_ADMIN)

                .antMatchers(HttpMethod.GET, "/company-objectives/{coId}/company-key-results/**").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.GET, "/company-objectives/{coId}/company-key-results").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.PATCH, "/company-objectives/{coId}/company-key-results/**").hasRole(CO_OKR_ADMIN)
                .antMatchers(HttpMethod.PUT, "/company-objectives/{coId}/company-key-results/**").hasRole(CO_OKR_ADMIN)
                .antMatchers(HttpMethod.POST, "/company-objectives/{coId}/company-key-results").hasRole(CO_OKR_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/company-objectives/{coId}/company-key-results/**").hasRole(CO_OKR_ADMIN)

                .antMatchers(HttpMethod.GET, "/business-unit-objectives/**").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.GET, "/business-unit-objectives").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.PATCH, "/business-unit-objectives/**").hasRole(BUO_OKR_ADMIN)
                .antMatchers(HttpMethod.POST, "/business-unit-objectives").hasRole(BUO_OKR_ADMIN)
                .antMatchers(HttpMethod.PUT, "/business-unit-objectives/**").hasRole(BUO_OKR_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/business-unit-objectives/**").hasRole(BUO_OKR_ADMIN)

                .antMatchers(HttpMethod.GET, "/business-unit-objectives/{buoId}/business-unit-key-results/**").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.GET, "/business-unit-objectives/{buoId}/business-unit-key-results").hasAnyRole(CO_OKR_ADMIN, BUO_OKR_ADMIN, READ_ONLY_USER)
                .antMatchers(HttpMethod.PATCH, "/business-unit-objectives/{buoId}/business-unit-key-results/**").hasRole(BUO_OKR_ADMIN)
                .antMatchers(HttpMethod.PUT, "/business-unit-objectives/{buoId}/business-unit-key-results/**").hasRole(BUO_OKR_ADMIN)
                .antMatchers(HttpMethod.POST, "/business-unit-objectives/{buoId}/business-unit-key-results").hasRole(BUO_OKR_ADMIN)
                .antMatchers(HttpMethod.POST, "/business-unit-objectives/{buoId}/business-unit-key-results/{id}/link-with-company-key-result").hasRole(BUO_OKR_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/business-unit-objectives/{buoId}/business-unit-key-results/**").hasRole(BUO_OKR_ADMIN)

                .and().httpBasic();
    }
}
