package com.payswiff.mfmsproject; // Package declaration for the application

import org.springframework.boot.SpringApplication; // Importing the SpringApplication class to run the application
import org.springframework.boot.autoconfigure.SpringBootApplication; // Importing the SpringBootApplication annotation

/**
 * The entry point for the MFMS Project application.
 * This class contains the main method which is the starting point of the Spring Boot application.
 */
@SpringBootApplication // Annotation that enables auto-configuration, component scanning, and Spring Boot's configuration support
public class MfmsProjectApplication {

    /**
     * The main method which serves as the entry point for the Spring Boot application.
     * It calls the run method of the SpringApplication class to start the application context.
     *
     * @param args Command line arguments passed during application startup
     */
    public static void main(String[] args) {
        // Running the Spring Boot application with the specified arguments
        SpringApplication.run(MfmsProjectApplication.class, args);
    }
}
