/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.security.tokens;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.function.Function;

public interface JwtTokenService {
	String generateToken(Authentication authentication);

	String extractUsername(String token);

	Date extractExpiration(String token);

	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	Claims extractAllClaims(String token);

	boolean validateToken(String token, String username);
}
