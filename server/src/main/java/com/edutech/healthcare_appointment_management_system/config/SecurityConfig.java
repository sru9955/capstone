package com.edutech.healthcare_appointment_management_system.config;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.edutech.healthcare_appointment_management_system.jwt.JwtRequestFilter;
import com.edutech.healthcare_appointment_management_system.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfigurationSource()).and()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/user/login").permitAll()
                .antMatchers("/api/patient/register").permitAll()
                .antMatchers("/api/doctor/register").permitAll()
                .antMatchers("/api/receptionist/register").permitAll()
                .antMatchers("/uploads/**").permitAll()
                .antMatchers(org.springframework.http.HttpMethod.POST, "/api/medical-records/**").hasAuthority("DOCTOR")
                .antMatchers(org.springframework.http.HttpMethod.GET, "/api/medical-records/patient/**").authenticated()
                .antMatchers(org.springframework.http.HttpMethod.GET, "/api/medical-records/*").authenticated()
                .antMatchers(org.springframework.http.HttpMethod.GET, "/api/medical-records/**").hasAnyAuthority("RECEPTIONIST", "ADMIN")
                .antMatchers("/api/patient/doctors").authenticated()
                .antMatchers("/api/patient/**").hasAuthority("PATIENT")
                .antMatchers("/api/doctor/**").hasAuthority("DOCTOR")
                .antMatchers("/api/receptionist/**").hasAuthority("RECEPTIONIST")
                .antMatchers("/api/admin/**").hasAuthority("ADMIN")
                .antMatchers("/api/notifications/**").authenticated()
                .antMatchers("/api/dashboard/**").authenticated()
                .antMatchers("/api/chat/**").authenticated()
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.Arrays.asList(
                "http://localhost:3000", 
                "http://localhost:4200",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:4200"
        ));
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(java.util.Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(java.util.Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
