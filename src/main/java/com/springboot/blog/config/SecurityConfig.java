package com.springboot.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//to enable method level security
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    // above code is to use DB authentication

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // For DB authentication, here we do not need to provide userDetailsService and passwordEncoder
    //to AuthenticationManager explicitly, it will internally  take care of everything
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()// Allow GET requests to api/**
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated())  // Authenticate all other requests
                .httpBasic(Customizer.withDefaults());  // Enable HTTP Basic Authentication
        return http.build();

//        http
//                .authorizeRequests()
//                .anyRequest().authenticated() // Requires authentication for all requests
//                .and()
//                .httpBasic();  // Enable Basic Authentication
//                return http.build();
        //above code is the not modern approach that is older style without lambda fpr old spring security version but still valid ion new versions
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails sumit = User.builder()
//                .username("sumit")
//                //.password("sumit25")   as it was a plain text
//                .password(passwordEncoder().encode("sumit25"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                //.password("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        System.out.println("Encoded password for admin: " + passwordEncoder().encode("admin"));
//        return new InMemoryUserDetailsManager(sumit, admin);
//    }


}