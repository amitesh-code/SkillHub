package com.project.SkillHub.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Model.Users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	private SecretKey getKey() {
	    return Keys.hmacShaKeyFor(
	            secret.getBytes(StandardCharsets.UTF_8)
	    );
	}
	public String generateToken(Users user) {
		
		Date now =new Date();
		Date expiry=new Date(now.getTime()+expiration);
		
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("userId", user.getId())
				.claim("role", user.getRole().name())
				.claim("tenantId", user.getTenant()!=null?user.getTenant().getId():null)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(getKey())
				.compact();
		
	}
	public String extractUsername(String token) {

	   
	   
	    return Jwts.parser()
	    		.verifyWith(getKey())
	    		.build()
	    		.parseSignedClaims(token)
	    		.getPayload()
	    		.getSubject();
	    
	}
	public Date extractExpiration(String token) {

	   

	    return Jwts.parser()
	            .verifyWith(getKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .getExpiration();
	}
	private boolean isTokenExpired(String token) {

	    return extractExpiration(token).before(new Date());
	}
	
	public Boolean validateToken(String token,UserDetails userDetails) {
		String username=extractUsername(token);
		return username.equals(userDetails.getUsername())&&!isTokenExpired(token);
	}
}
