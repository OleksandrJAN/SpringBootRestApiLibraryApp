package com.libraryApp.restAPI.config;

import com.libraryApp.restAPI.security.jwt.JwtUserDetailsService;
import com.libraryApp.restAPI.security.jwt.JwtAuthenticationEntryPoint;
import com.libraryApp.restAPI.security.jwt.JwtConfigurer;
import com.libraryApp.restAPI.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Autowired
    public WebSecurityConfig(
            JwtTokenProvider jwtTokenProvider,
            JwtUserDetailsService jwtUserDetailsService,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //                .headers().addHeaderWriter(
                //                        new StaticHeadersWriter("Access-Control-Allow-Origin", "http://localhost:4200")
                //                );
                    .authorizeRequests()
                    .antMatchers("/auth/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }


}
