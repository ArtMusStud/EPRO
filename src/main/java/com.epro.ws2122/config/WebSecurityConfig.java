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
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/company-objectives/**").permitAll()
                .antMatchers(HttpMethod.GET, "/company-objectives").permitAll()
                .antMatchers(HttpMethod.PATCH, "/company-objectives/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/company-objectives/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/company-objectives/**").permitAll()

                .antMatchers(HttpMethod.GET, "/company-objectives/{coId}/company-key-results/**").permitAll()
                .antMatchers(HttpMethod.GET, "/company-objectives/{coId}/company-key-results").permitAll()
                .antMatchers(HttpMethod.PATCH, "/company-objectives/{coId}/company-key-results/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/company-objectives/{coId}/company-key-results/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/company-objectives/{coId}/company-key-results/**").permitAll()

                .anyRequest().authenticated();
    }
}
