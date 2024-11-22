package com.payswiff.mfmsproject.configs; // Package declaration for configuration classes

import io.swagger.v3.oas.models.OpenAPI; // Importing the OpenAPI model class
import io.swagger.v3.oas.models.info.Contact; // Importing the Contact model for API information
import io.swagger.v3.oas.models.info.Info; // Importing the Info model for API metadata
import io.swagger.v3.oas.models.info.License; // Importing the License model for API licensing information
import io.swagger.v3.oas.models.security.SecurityRequirement; // Importing SecurityRequirement for API security schemes
import io.swagger.v3.oas.models.security.SecurityScheme; // Importing SecurityScheme for defining security protocols
import io.swagger.v3.oas.models.Components; // Importing Components to hold reusable components of the API

import org.springdoc.core.models.GroupedOpenApi; // Importing GroupedOpenApi for grouping API paths
import org.springframework.context.annotation.Bean; // Importing the Bean annotation to declare bean methods
import org.springframework.context.annotation.Configuration; // Importing the Configuration annotation for configuration classes

/**
 * Swagger configuration class for the Merchant Feedback Management System (MFMS).
 * <p>
 * This class configures the Swagger API documentation for the MFMS application, 
 * setting up various metadata, licensing, and security schemes. The class defines the 
 * OpenAPI documentation, including API information (title, version, contact details) 
 * and sets up the security configuration for JWT-based authentication. It also provides 
 * the option to group API paths for organizational purposes.
 * </p>
 * <p>
 * Key configurations include:
 * <ul>
 *   <li>API metadata such as title, description, version, contact, and license information.</li>
 *   <li>Security scheme definition for JWT authentication with a bearer token.</li>
 *   <li>Grouping of API endpoints for better organization and filtering.</li>
 * </ul>
 * </p>
 *
 * @version MFMS_0.0.1
 * @author Gopi Bapanapalli
 */

@Configuration // Annotation that indicates this class provides Spring configuration
public class SwaggerConfig {

    /**
     * Bean for customizing the OpenAPI documentation.
     * This includes API metadata, contact information, licensing, and security scheme definition.
     *
     * @return OpenAPI instance with custom configurations
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info() // Set up basic information about the API
                .title("Merchant Feedback Management System") // API title
                .description("Feedback collection from merchant about POS devices") // API description
                .version("MFMS_0.0.1") // API version
                .contact(new Contact() // Contact information for API
                    .name("MFMS") // Contact name
                    .url("www.mfms.com") // Contact URL
                    .email("mfmsproject7@gmail.com")) // Contact email
                .license(new License() // License information
                    .name("MFMS License") // License name
                    .url("www.mfms.com"))) // License URL
            .components(new Components() // Define reusable components for the API
                .addSecuritySchemes("bearer-key", // Name of the security scheme
                    new SecurityScheme() // Creating a new SecurityScheme instance
                        .type(SecurityScheme.Type.HTTP) // Define type as HTTP
                        .scheme("bearer") // Set scheme as bearer
                        .bearerFormat("JWT") // Specify the format as JWT
                        .in(SecurityScheme.In.HEADER) // Set location of the key in the header
                        .name("Authorization") // Name of the authorization header
                ))
            .addSecurityItem(new SecurityRequirement().addList("bearer-key")); // Apply the security scheme to the API
    }

    /**
     * Optional bean for grouping APIs.
     * This can be used for specific path matching and grouping related endpoints together.
     *
     * @return GroupedOpenApi instance for public APIs
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder() // Build a new GroupedOpenApi instance
            .group("public") // Name of the group
            .pathsToMatch("/**") // Match all paths for this group
            .build(); // Build the GroupedOpenApi instance
    }
}
