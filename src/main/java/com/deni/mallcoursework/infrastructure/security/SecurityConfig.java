package com.deni.mallcoursework.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private final MallUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(MallUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                            .requestMatchers("/register", "/login", "/css/**").permitAll()
                            .anyRequest().authenticated()
                )
                .formLogin(customizer -> {
                    customizer.loginPage("/login");
                    customizer.usernameParameter("email");
                })
                .logout(c -> {
                    c.logoutUrl("/logout");
                    c.logoutSuccessUrl("/login");
                    c.clearAuthentication(true);
                    c.invalidateHttpSession(true);
                })
                .rememberMe(rememberConf -> rememberConf.rememberMeServices(getRememberMeServices(userDetailsService)));
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

    @Bean
    RememberMeServices getRememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices(
                KEY,
                userDetailsService,
                TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        return rememberMe;
    }
}
