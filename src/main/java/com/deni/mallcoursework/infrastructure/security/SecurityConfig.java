package com.deni.mallcoursework.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String KEY = "myKey";
    private static final String[] ALLOW_ANONYMOUS_LIST = {
            "/", "/register", "/login", "/css/**", "/js/**", "/bootstrap/css/**", "/bootstrap/js/**",
            "/products", "/products/{id}"
    };

    private final RememberMeServices rememberMeServices;

    @Autowired
    public SecurityConfig(MallUserDetailsService userDetailsService) {
        rememberMeServices = new TokenBasedRememberMeServices(KEY, userDetailsService);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                            .requestMatchers(ALLOW_ANONYMOUS_LIST).permitAll()
                            .anyRequest().authenticated()
                )
                .formLogin(configurer -> {
                    configurer.loginPage("/login");
                    configurer.usernameParameter("email");
                })
                .logout(configurer -> {
                    configurer.logoutUrl("/logout");
                    configurer.logoutSuccessUrl("/login");
                    configurer.clearAuthentication(true);
                    configurer.invalidateHttpSession(true);
                })
                .rememberMe(rememberConf ->
                        rememberConf.rememberMeServices(rememberMeServices)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
