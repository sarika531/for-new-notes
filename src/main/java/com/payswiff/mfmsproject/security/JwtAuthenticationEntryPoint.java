package com.payswiff.mfmsproject.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom implementation of the AuthenticationEntryPoint interface.
 * This class handles unauthorized access attempts and sends appropriate HTTP error responses.
 * @authorGopi Bapanapalli
 * @version MFMS_0.0.1
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commences an authentication scheme.
     * This method is invoked when an AuthenticationException is thrown.
     *
     * @param request the HttpServletRequest object that contains the request the client made
     * @param response the HttpServletResponse object that will contain the response to the client
     * @param authException the exception that was thrown during authentication
     * @throws IOException if an input or output error occurs
     * @throws ServletException if the request for the GET could not be handled
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        // Send an HTTP 401 Unauthorized response with the message from the authentication exception.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
