package com.shashank.musiclibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;
    // Define Security Filter Chain to configure HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Disable CSRF as it's not needed for stateless authentication
                .authorizeHttpRequests(  authz -> authz.
                        requestMatchers("/api/users/signup", "/api/users/login","/api/users/update-password")
                        .permitAll().requestMatchers("/api/users","/api/users/","/api/users/add-user").hasRole("ADMIN")

                        .requestMatchers(
                                "/api/artists", "/api/artists/", "/api/artists/artist", "/api/artists/update/", "/api/artists/delete/",
                                "/api/albums", "/api/albums/", "/api/albums/add-album", "/api/albums/update/", "/api/albums/delete/",
                                "/api/tracks", "/api/tracks/", "/api/tracks/add-track", "/api/tracks/update/", "/api/tracks/delete/",
                                "/api/users/update-password")  // Allow editors to update their own password
                        .hasRole("EDITOR")

                        .requestMatchers("/api/favorites/", "/api/favorites/add-favorite", "/api/favorites/remove-favorite/","/api/users/logout").authenticated()
                        .anyRequest()
                        .authenticated())
                        .httpBasic(Customizer.withDefaults())  // Basic Authentication
                        .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();

    }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
   }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }
}
