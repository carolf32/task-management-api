package com.example.tasks_management.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tasks_management.exceptions.customExceptions.NotFoundException;
import com.example.tasks_management.user.UserEntity;
import com.example.tasks_management.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
    
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;

    public SecurityFilter(JwtConfig jwtConfig, UserRepository userRepository){
        this.jwtConfig=jwtConfig;
        this.userRepository=userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain)
    throws ServletException, IOException{
        
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.replace("Bearer ", "");
            Long userId = jwtConfig.getIdFromToken(token);

            UserEntity user = userRepository.findById(userId)
            .orElseThrow(()->new NotFoundException("User not found"));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
