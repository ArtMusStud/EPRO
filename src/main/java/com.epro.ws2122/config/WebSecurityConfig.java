package com.epro.ws2122.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* ToDo:
            - permitAll() überarbeiten
            - BUO OKR Admins dürfen nur ihre eigenen Resourcen bearbeiten
         */
        http
                .authorizeRequests()

                .antMatchers(HttpMethod.GET, "/company-objectives/**").hasAnyRole("CO OKR Admin", "BUO OKR Admin")
                .antMatchers(HttpMethod.GET, "/company-objectives").hasAnyRole("CO OKR Admin", "BUO OKR Admin")
                .antMatchers(HttpMethod.PATCH, "/company-objectives/**").hasRole("CO OKR Admin")
                .antMatchers(HttpMethod.PUT, "/company-objectives/**").hasRole("CO OKR Admin")
                .antMatchers(HttpMethod.DELETE, "/company-objectives/**").hasRole("CO OKR Admin")

                .antMatchers(HttpMethod.GET, "/company-objectives/{coId}/company-key-results/**").hasAnyRole("CO OKR Admin", "BUO OKR Admin")
                .antMatchers(HttpMethod.GET, "/company-objectives/{coId}/company-key-results").hasAnyRole("CO OKR Admin", "BUO OKR Admin")
                .antMatchers(HttpMethod.PATCH, "/company-objectives/{coId}/company-key-results/**").hasRole("BUO OKR Admin")
                .antMatchers(HttpMethod.PUT, "/company-objectives/{coId}/company-key-results/**").hasRole("BUO OKR Admin")
                .antMatchers(HttpMethod.DELETE, "/company-objectives/{coId}/company-key-results/**").hasRole("BUO OKR Admin");
    }
}
