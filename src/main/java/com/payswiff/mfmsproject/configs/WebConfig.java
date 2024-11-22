package com.payswiff.mfmsproject.configs; // Package declaration for configuration classes

import org.springframework.context.annotation.Configuration; // Importing Configuration annotation for Spring configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Importing CorsRegistry for CORS configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Importing WebMvcConfigurer to customize Spring MVC configuration

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) settings.
 * <p>
 * This class implements {@link WebMvcConfigurer} to customize the default Spring MVC 
 * settings, specifically for handling Cross-Origin Resource Sharing (CORS) configurations.
 * CORS allows restricted resources on a web server to be requested from another domain outside 
 * the domain from which the resource originated. This configuration is important for enabling 
 * the frontend application to interact with the backend API securely and efficiently.
 * </p>
 *
 * The CORS settings defined in this class:
 * <ul>
 *   <li>Allow all endpoints in the application ("/**").</li>
 *   <li>Allow requests from specific origins, such as frontend URLs (e.g., "http://localhost:5173").</li>
 *   <li>Allow specific HTTP methods including GET, POST, PUT, DELETE, and OPTIONS.</li>
 *   <li>Allow all headers to be sent in requests.</li>
 *   <li>Enable credentials (cookies, authorization headers) to be included in cross-origin requests.</li>
 * </ul>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */
@Configuration // Annotation indicating that this class provides Spring configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Method to configure CORS mappings for the application.
     * This allows specific origins, HTTP methods, and headers for cross-origin requests.
     *
     * @param registry CORS registry to add mappings to
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints in the application
                .allowedOrigins("http://localhost:5173","http://192.168.2.7:5173") // Specify allowed origin (frontend URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
                .allowedHeaders("*") // Allow all headers in requests
                .allowCredentials(true); // Allow credentials such as cookies or authorization headers
    }
}
