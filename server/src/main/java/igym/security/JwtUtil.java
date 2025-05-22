package igym.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * Utility class for JWT (JSON Web Token) operations.
 * Handles token generation, validation, and claims extraction.
 *
 * <p>
 * This component provides functionality for:
 * - Generating JWT tokens for authenticated users
 * - Validating token authenticity and expiration
 * - Extracting user information from tokens
 * </p>
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInSec;

    /**
     * Generates a new JWT token for an authenticated user.
     *
     * @param userId the UUID of the authenticated user
     * @param username the username of the authenticated user
     * @return a signed JWT token containing user information
     */
    public String generateToken(UUID userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInSec * 1000);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the claims contained in the token
     * @throws Exception if the token is invalid or malformed
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates a JWT token by checking its signature and expiration.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the username claim from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the username stored in the token
     */
    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    /**
     * Extracts the user ID claim from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the UUID of the user stored in the token
     */
    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(extractAllClaims(token).getSubject());
    }

    /**
     * Creates a signing key for JWT token generation and validation.
     *
     * @return a Key object suitable for HMAC-SHA256 signing
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
} 