package com.payswiff.mfmsproject.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GetPassWordEncoder {
	private static final Logger logger =LogManager.getLogger(GetPassWordEncoder.class);
	public static void main(String[] args) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("securePassword123"));
		
		logger.info("This is an info log message");
        logger.debug("This is a debug log message");
        logger.warn("This is a warning log message");
        logger.error("This is an error log message");
  	}
}
