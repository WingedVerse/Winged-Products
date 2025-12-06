package com.cvgenie.backend.jwt;

import com.cvgenie.backend.entity.ApiResponse;
import com.cvgenie.backend.entity.User;
import com.cvgenie.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                email = jwtHelper.extractEmail(jwtToken);
            } catch (Exception e) {
                // Log the exception and set an appropriate error message if needed
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        // Validate token and set user information for the request if authentication is not already set
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            ApiResponse<User> user = userService.getByEmail(email); // Fetch your User entity
            if (user.getData() != null && !jwtHelper.isTokenExpired(jwtToken)) {
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(user.getData());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
