package com.example.tasks_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

   private final SecurityFilter securityFilter;

   public SecurityConfig(SecurityFilter securityFilter){
      this.securityFilter=securityFilter;
   }
    
@Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http.csrf().disable()
    .authorizeHttpRequests()
    .requestMatchers("/auth/**")
    .permitAll()
    .anyRequest().authenticated()
    .and()
    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
   }
}
