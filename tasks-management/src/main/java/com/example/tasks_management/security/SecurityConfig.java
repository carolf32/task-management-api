package com.example.tasks_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.tasks_management.user.UserRepository;

@Configuration
public class SecurityConfig {
    
@Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtConfig jwtConfig, UserRepository userRepository) throws Exception{

      SecurityFilter securityFilter = new SecurityFilter(jwtConfig, userRepository);

      http.csrf().disable()
      .authorizeHttpRequests()
      .requestMatchers("/auth/**", "/users")
      .permitAll()
      .anyRequest().authenticated()
      .and()
      .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
   }
}
