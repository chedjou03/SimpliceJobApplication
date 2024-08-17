package com.chedjouJobPortal.jobportal.config;


import com.chedjouJobPortal.jobportal.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class WebSecurityConfig {

    @Autowired
    private  CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomAutenticationSuccessHandler customAutenticationSuccessHandler;

    private final String[] publicUrl = {"/",
                                            "/global-search/**",
                                            "/register",
                                            "/register/**",
                                            "/webjars/**",
                                            "/resources/**",
                                            "/assets/**",
                                            "/css/**",
                                            "/summernote/**",
                                            "/js/**",
                                            "/*.css",
                                            "/*.js",
                                            "/*.js.map",
                                            "/fonts**", "/favicon.ico", "/resources/**", "/error"};



    @Bean
    protected SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        
        http.authorizeHttpRequests(auth->{
            auth.requestMatchers(publicUrl).permitAll();
            auth.anyRequest().authenticated();
        });

        http.formLogin(form -> form.loginPage("/login").permitAll()
                                .successHandler(customAutenticationSuccessHandler))
                                .logout(logout -> {
                                    logout.logoutUrl("/logout");
                                    logout.logoutSuccessUrl("/");
                                    })
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    // This method tell spring security how to find our users and how to authenticate passwords
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return  authenticationProvider;
    }

    // This tell spring security how to authenticate password (plain text or encrypted)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
