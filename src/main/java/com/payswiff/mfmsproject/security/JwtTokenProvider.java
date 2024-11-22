package com.payswiff.mfmsproject.security;

import java.security.Key;
import java.sql.Date;
import java.util.Iterator;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
/**
 * Represents a request to create a new Merchant.
 * <p>This class is used to encapsulate the data required for creating a new Merchant entity in the system. 
 * It contains the merchant's email, name, phone number, business name, and business type. 
 * The data collected through this request can then be used to create a new Merchant instance.</p>
 * 
 * <p>Note: This class leverages Lombok annotations for boilerplate code reduction and includes a 
 * method for converting the request into a Merchant entity with a generated UUID.</p>
 * 
 * @version MFMS_0.0.1
 * @author Gopi Bpanapalli
 */
@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecretKey;
	@Value("${app-jwt-expiration-milliseconds}")
	private Long jwtExpiration;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	// generate jwt token
	public String generateToken(Authentication authentication) {

		String username = authentication.getName();
		java.util.Date currentDate =  new java.util.Date();

		java.util.Date expireDate = new java.util.Date(currentDate.getTime() + jwtExpiration);

		String token = Jwts.builder().subject(username).issuedAt(currentDate).expiration(expireDate).signWith(key())
				.compact();
		return token;

	}

	// generate key
	public Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
	}

	// get username from jwt token
	public String getUsername(String token) {

		return Jwts.parser()
				.verifyWith((SecretKey) key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	
	//validate jwt token
	public boolean validate(String token) throws Exception
	{
		
		try {
			Jwts.parser()
			.verifyWith((SecretKey) key())
			.build()
			.parse(token);
			
			
			
		} catch (MalformedJwtException e) {
			// TODO: handle exception
			
			if (token instanceof String) {
			    logger.info("Your token is: {}", token);
			} else {
			    logger.info("Token is not a string, it is of type: {}", token.getClass().getName());
			}
			throw new Exception("Invalid jwt token");
		}
		catch (ExpiredJwtException e) {
			// TODO: handle exception
			throw new Exception("jwt token expired");
		}
		catch (UnsupportedJwtException e) {
			// TODO: handle exception
			throw new Exception("unsupported jwt token");
		}
		catch (IllegalArgumentException e) {
			// TODO: handle exception
			throw new Exception("String is empty");
		}
		return true;
	}
}
