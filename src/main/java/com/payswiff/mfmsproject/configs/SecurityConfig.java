

package com.payswiff.mfmsproject.configs; // Package declaration for configuration classes

import org.springframework.beans.factory.annotation.Autowired; // Importing the Autowired annotation for dependency injection
import org.springframework.context.annotation.Bean; // Importing the Bean annotation to declare bean methods
import org.springframework.context.annotation.Configuration; // Importing the Configuration annotation for configuration classes
import org.springframework.http.HttpMethod; // Importing the HttpMethod class for HTTP method types
import org.springframework.security.authentication.AuthenticationManager; // Importing the AuthenticationManager interface
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importing the UsernamePasswordAuthenticationToken class
import org.springframework.security.config.Customizer; // Importing Customizer for configuring HTTP security
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importing AuthenticationConfiguration for authentication settings
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Importing EnableMethodSecurity to enable method-level security
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importing HttpSecurity to configure web-based security
import org.springframework.security.config.http.SessionCreationPolicy; // Importing SessionCreationPolicy to manage session creation
import org.springframework.security.core.userdetails.User; // Importing User for user details
import org.springframework.security.core.userdetails.UserDetails; // Importing UserDetails for user detail service
import org.springframework.security.core.userdetails.UserDetailsService; // Importing UserDetailsService for user management
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importing BCryptPasswordEncoder for password encoding
import org.springframework.security.crypto.password.PasswordEncoder; // Importing PasswordEncoder interface for password encoding
import org.springframework.security.provisioning.InMemoryUserDetailsManager; // Importing InMemoryUserDetailsManager for in-memory user management
import org.springframework.security.web.SecurityFilterChain; // Importing SecurityFilterChain for security filter chain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importing filter for username/password authentication

import com.payswiff.mfmsproject.security.JwtAuthenticationEntryPoint; // Importing custom JWT authentication entry point
import com.payswiff.mfmsproject.security.JwtAuthenticationFilter; // Importing custom JWT authentication filter
import com.payswiff.mfmsproject.security.JwtTokenProvider; // Importing custom JWT token provider

/**
 * Security configuration class for the MFMS Project.
 * <p>
 * This class provides the configuration for web-based security in the MFMS application. 
 * It configures authentication and authorization rules, integrating JWT-based authentication 
 * to secure API endpoints. The class defines which HTTP requests can be accessed based on user roles 
 * (e.g., 'admin', 'employee') and specifies the rules for securing access to various resources in the system.
 * </p>
 * <p>
 * The class includes settings for:
 * <ul>
 *   <li>Disabling CSRF protection for stateless authentication.</li>
 *   <li>Defining access control for different HTTP endpoints based on roles.</li>
 *   <li>Setting up JWT authentication filters and entry points.</li>
 *   <li>Enabling basic authentication.</li>
 *   <li>Managing stateless session policy for RESTful APIs.</li>
 * </ul>
 * </p>
 *
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

@Configuration // Annotation that indicates this class provides Spring configuration
@EnableMethodSecurity // Enables method-level security
public class SecurityConfig {

    // Autowired dependencies for JWT authentication and user management
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Entry point for handling unauthorized requests
    @Autowired
    JwtTokenProvider jwtTokenProvider; // Provider for creating and validating JWT tokens
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter; // Filter for JWT authentication

    @Autowired
    UserDetailsService userDetailsService; // Service for loading user-specific data

    /**
     * Bean for password encoding using BCrypt.
     * This bean is used to securely hash passwords.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Returns a BCryptPasswordEncoder for hashing passwords
    }

    /**
     * Bean for the AuthenticationManager.
     * This manager handles authentication requests.
     *
     * @param configuration The authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if an error occurs during authentication configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // Retrieves the AuthenticationManager from the configuration
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param httpSecurity The HttpSecurity object for configuring web security
     * @return SecurityFilterChain object
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests((authorize) -> authorize
                        // Allow access to Swagger and API documentation endpoints
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        
                        // Allow unauthenticated access to certain authentication and employee creation endpoints
                        .requestMatchers(HttpMethod.POST, "/api/authentication/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/authentication/forgotpassword").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/employees/create").permitAll()

                        // Allow access to admin-only endpoints with 'admin' role
                        .requestMatchers(HttpMethod.POST, "/api/devices/create").hasRole("admin")
                        .requestMatchers(HttpMethod.POST, "/api/merchants/create").hasRole("admin")
                        .requestMatchers(HttpMethod.POST, "/api/MerchantDeviceAssociation/assign").hasRole("admin")
                        .requestMatchers(HttpMethod.POST, "/api/questions/create").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/MerchantDeviceAssociation/get/merchantdeviceslist").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/MerchantDeviceAssociation/check/merchant-device").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/allfeedbackscount").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/average-rating-by-device").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/MerchantDeviceAssociation/device-count").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/device-count").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/employees/all").hasRole("admin")
                        .requestMatchers(HttpMethod.GET,"/api/FeedbackQuestions/**").hasRole("admin")
                        
                        // Access to employee endpoints with 'employee' role
                        .requestMatchers(HttpMethod.POST, "/api/feedback/create").hasRole("employee")
                        
                        // Access to endpoints requiring either admin or employee authentication
                        .requestMatchers(HttpMethod.GET, "/api/devices/get").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/devices/all").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/employees/get").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/feedback/getallfeedbacks").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/merchants/get").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/merchants/all").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/questions/get").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/questions/getbydesc").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/questions/all").authenticated()

                        // Permit all other requests
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults()) // Enable basic authentication
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Handle unauthorized requests
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Configure stateless session management

        // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build(); // Build the security filter chain
    }
}
