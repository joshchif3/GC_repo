package myfiles.GC.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "JoshWebs"; // Use a more secure key in production
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // Generate a JWT token from the authentication object
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();  // Get username from the authenticated user
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);  // Set expiration time

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC512(SECRET_KEY));  // Use the secret key to sign the token
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get Authentication from token
    public Authentication getAuthentication(String token) {
        String username = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();

        return new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")));
    }
}
