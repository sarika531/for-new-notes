package com.payswiff.mfmsproject.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A filter that processes JWT authentication for incoming requests.
 * This class extends OncePerRequestFilter to ensure that the filter is
 * invoked once per request. 
 *  @authorGopi Bapanapalli
 * @version MFMS_0.0.1
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Responsible for generating and validating JWT tokens

    @Autowired
    private UserDetailsService userDetailsService; // Service to load user details for authentication

    /**
     * Processes the incoming request to authenticate the user based on the JWT token.
     *
     * @param request  the HttpServletRequest object that contains the request
     * @param response the HttpServletResponse object that will contain the response
     * @param filterChain the filter chain to continue processing the request
     * @throws ServletException if an error occurs during request processing
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve JWT token from the HTTP request
        String token = getTokenFromRequest(request);

        // Validate the token and set the authentication in the context if valid
        try {
            if (org.springframework.util.StringUtils.hasText(token) && jwtTokenProvider.validate(token)) {
                // Extract username from the token
                String username = jwtTokenProvider.getUsername(token);

                // Load user details from the database using the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication token
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set details for the authentication token
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Log the exception (could be enhanced with a logging framework)
            e.printStackTrace();
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * @param request the HttpServletRequest from which to extract the token
     * @return the extracted JWT token, or null if not found or invalid
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Check if the header contains a Bearer token
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Return the token without the "Bearer " prefix
            return bearerToken.substring(7);
        }
        return null; // No valid token found
    }
}
